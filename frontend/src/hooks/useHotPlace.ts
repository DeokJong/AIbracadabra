import { KakaoMapMarkerPropsWithInfo, useKakaoMap } from '@/hooks/useKakaoMap'
import { CommonResponse } from '@/service/common'
import axios from 'axios'
import { storeToRefs } from 'pinia'
import { useToast } from 'vue-toastification'

export type FullHotPlace = HotPlace & {
  imageUrl: string
  hno: number
  mno: number
}

export type HotPlace = {
  overview: string
  title: string
  mapX: number
  mapY: number
}

const useHotPlace = () => {
  const kakaoMap = useKakaoMap()
  const toast = useToast()
  const { markerProps, traceMapProps } = storeToRefs(kakaoMap)

  const removeHotPlace = async (hno: number) => {
    try {
      await axios.delete(`/api/v1/hotPlace/${hno}`)
      // 핫 플레이스 삭제 후 마커 갱신
      await fetchHotPlaces()
      toast.success('핫 플레이스가 삭제되었습니다.')

    } catch (error) {
      if (axios.isAxiosError(error)) {
        if (error.response?.status === 404) {
          toast.error('핫 플레이스가 존재하지 않습니다.')
          return
        } else if (error.response?.status === 500) {
          toast.error('서버 오류로 핫 플레이스 삭제에 실패했습니다. 잠시 후 다시 시도해주세요.')
          return
        }
        console.error('핫 플레이스 삭제 중 오류 발생:', error.response?.data)
      }
      toast.error('핫 플레이스 삭제 중 오류가 발생했습니다. 다시 시도해주세요.')
    }
  }

  const registerHotPlace = async (hotPlace: HotPlace, imageFile?: File) => {
    const formData = new FormData()
    const hotPlaceBlob = new Blob([JSON.stringify(hotPlace)], {
      type: 'application/json',
    })
    formData.append('hotPlace', hotPlaceBlob)
    if (imageFile) {
      formData.append('image', imageFile)
    }
    try {
      await axios.post('/api/v1/hotPlace', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      })
      toast.success('핫 플레이스가 등록되었습니다.')
      await fetchHotPlaces()
      return true
    } catch (error) {
      if (axios.isAxiosError(error)) {
        if (error.response?.status === 400) {
          toast.error('핫 플레이스 등록에 실패했습니다. 입력값을 확인해주세요.')
          return
        } else if (error.response?.status === 500) {
          toast.error('서버 오류로 핫 플레이스 등록에 실패했습니다. 잠시 후 다시 시도해주세요.')
          return
        }
        console.error('핫 플레이스 등록 중 오류 발생:', error.response?.data)
      }
      toast.error('핫 플레이스 등록 중 오류가 발생했습니다. 다시 시도해주세요.')
    }
    return false
  }

  const fetchHotPlaces = async () => {
    try {
      const response = await axios.get<CommonResponse<FullHotPlace[]>>(
        `/api/v1/hotPlace?mapX=${traceMapProps.value.lng}&mapY=${traceMapProps.value.lat}${mapLevelIntoRadius(traceMapProps.value.level ?? 10000)}`
      )
      const hotPlaces = response.data.data
      const newMarkers: KakaoMapMarkerPropsWithInfo[] = []
      hotPlaces.forEach((ele) => {
        newMarkers.push({
          lng: ele.mapX,
          lat: ele.mapY,
          image: {
            imageSrc: `/marker/HOTPLACE.png`,
            imageHeight: 32,
            imageWidth: 32,
          },
          clickable: true,
          info: {
            contentId: 'hotplace_' + ele.hno,
          },
        })
      })
      markerProps.value = newMarkers
    } catch (error) {
      console.error('Error fetching hot places:', error)
    }
  }

  return {
    fetchHotPlaces,
    registerHotPlace,
    removeHotPlace
  }
}

export default useHotPlace

/**
 * 맵 레벨에 따른 반경 비율 구하는 함수
 * @param level
 * @returns
 */
const mapLevelIntoRadius = (level: number) => {
  const MULTIFLY = 10
  switch (level) {
    case 1:
      return 20 * MULTIFLY
    case 2:
      return 30 * MULTIFLY
    case 3:
      return 50 * MULTIFLY
    case 4:
      return 100 * MULTIFLY
    case 5:
      return 250 * MULTIFLY
    case 6:
      return 500 * MULTIFLY
    default:
      return 20000
  }
}
