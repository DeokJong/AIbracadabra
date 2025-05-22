import { CommonResponse } from '@/service/common'
import axios from 'axios'
import { defineStore } from 'pinia'
import { reactive, ref } from 'vue'
import { useToast } from 'vue-toastification'
import { KakaoMapMarkerProps, type KakaoMapProps } from 'vue3-kakao-maps'

export enum ContentType {
  TOURIST_SPOT = 'TOURIST_SPOT',
  CULTURE_FACILITY = 'CULTURE_FACILITY',
  FESTIVAL_EVENT = 'FESTIVAL_EVENT',
  TRAVEL_COURSE = 'TRAVEL_COURSE',
  LEISURE_SPORTS = 'LEISURE_SPORTS',
  ACCOMMODATION = 'ACCOMMODATION',
  SHOPPING = 'SHOPPING',
  RESTAURANT = 'RESTAURANT',
}

export const DEFAULT_KAKAO_MAP_PROPS: KakaoMapProps = {
  lng: 126.978652258309,
  lat: 37.566826004661,
  level: 3,
}

export type Document = {
  mapX: number
  mapY: number
  address: string
}

export type FullDocument = Document & {
  title: string
  firstImage: string
  contentsTypeId: string
  contentId: string
  overview: string
}

export type KakaoDocument<T> = {
  meta: {
    numOfRows: number
    pageNo: number
    totalCount: number
    end: boolean
  }
  documents: T[]
}

export type KakaoMapMarkerPropsWithInfo = KakaoMapMarkerProps & {
  info: { contentId: string }
}

export const useKakaoMap = defineStore('kakaoMap', () => {
  const toast = useToast()

  const kakaoMapProps = ref<KakaoMapProps>(DEFAULT_KAKAO_MAP_PROPS)
  const markerProps = ref<KakaoMapMarkerPropsWithInfo[]>([])
  const currentContent = reactive<FullDocument>({} as FullDocument)

  const locationSearch = async (query: string, pageNo: number = 1) => {
    await axios
      .get<CommonResponse<KakaoDocument<Document>>>(
        `/api/v1/map/location?query=${query}&pageNo=${pageNo}`
      )
      .then((response) => {
        const document: Document = response.data.data.documents[0]
        kakaoMapProps.value.lng = document.mapX
        kakaoMapProps.value.lat = document.mapY
      })
      .catch(() => {
        toast.warning('해당 지역에 조회된 값이 없습니다.')
      })
  }

  const contentSearch = async (contentType: ContentType, pageNo: number = 1) => {
    await axios
      .get<CommonResponse<KakaoDocument<FullDocument>>>(
        `/api/v1/map/contents?mapX=${kakaoMapProps.value.lng}&mapY=${kakaoMapProps.value.lat}&contentTypeId=${contentType}&pageNo=${pageNo}&radius=${mapLevelIntoRadius(kakaoMapProps.value.level ? kakaoMapProps.value.level : 1000)}`
      )
      .then((response) => {
        if (response.data.data.meta.numOfRows) {
          const document: FullDocument[] = response.data.data.documents
          markerProps.value = []
          document.forEach((ele) => {
            markerProps.value.push({
              lng: ele.mapX,
              lat: ele.mapY,
              image: {
                imageSrc: `/marker/${ContentTypeImageResolver(ele.contentsTypeId)}`,
                imageHeight: 32,
                imageWidth: 32,
              },
              clickable: true,
              info: {
                contentId: ele.contentId,
              },
            })
          })
        } else {
          toast.info("조회된 데이터가 없습니다")
        }
      })
      .catch(() => {
        toast.warning('잘못된 입력입니다')
      })
  }

  const contentDetailSearch = async (contentId: string) => {
    await axios
      .get<CommonResponse<KakaoDocument<FullDocument>>>(`/api/v1/map/contents/${contentId}`)
      .then((response) => {
        const document: FullDocument = response.data.data.documents[0]
        Object.assign(currentContent, document)
      })
      .catch(() => {
        toast.warning('잘못된 입력입니다')
      })
  }

  return {
    markerProps,
    kakaoMapProps,
    currentContent,
    locationSearch,
    contentSearch,
    contentDetailSearch,
  }
})

const ContentTypeImageResolver = (code: string, type: string = 'png'): string => {
  switch (code) {
    case '12':
      return `${ContentType.TOURIST_SPOT}.${type}`
    case '14':
      return `${ContentType.CULTURE_FACILITY}.${type}`
    case '15':
      return `${ContentType.FESTIVAL_EVENT}.${type}`
    case '25':
      return `${ContentType.TRAVEL_COURSE}.${type}`
    case '28':
      return `${ContentType.LEISURE_SPORTS}.${type}`
    case '32':
      return `${ContentType.ACCOMMODATION}.${type}`
    case '38':
      return `${ContentType.SHOPPING}.${type}`
    case '39':
      return `${ContentType.RESTAURANT}.${type}`
    default:
      // 알 수 없는 코드일 경우 기본 이미지
      return `UNKNOWN.${type}`
  }
}

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
