import { CommonResponse } from '@/service/common'
import axios from 'axios'
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { useToast } from 'vue-toastification'
import { type KakaoMapProps } from 'vue3-kakao-maps'

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
  title?: string
  firstImage?: string
  contentsTypeId?: string
  contentId?: string
}

export type KakaoDocument = {
  meta: {
    numOfRows: number
    pageNo: number
    totalCount: number
    end: boolean
  }
  documents: Document[]
}

export const useKakaoMap = defineStore('kakaoMap', () => {
  const toast = useToast()

  const kakaoMapProps = ref<KakaoMapProps>(DEFAULT_KAKAO_MAP_PROPS)

  const locationSearch = async (query: string, pageNo: number = 1) => {
    await axios
      .get<CommonResponse<KakaoDocument>>(`/api/v1/map/location?query=${query}&pageNo=${pageNo}`)
      .then((response) => {
        const document: Document = response.data.data.documents[0]
        console.log(document)
        kakaoMapProps.value.lng = document.mapX
        kakaoMapProps.value.lat = document.mapY
      })
      .catch(() => {
        toast.warning('잘못된 입력입니다')
      })
  }

  const contentSearch = async (contentType: ContentType, pageNo: number = 1) => {
    await axios
      .get<CommonResponse<KakaoDocument>>(
        `/api/v1/map/contents?mapX=${kakaoMapProps.value.lng}&mapY=${kakaoMapProps.value.lat}&contentTypeId=${contentType}&pageNo=${pageNo}`
      )
      .then((response) => {
        const document: Document[] = response.data.data.documents
        console.log(document)
      })
      .catch(() => {
        toast.warning('잘못된 입력입니다')
      })
  }

  const contentDetailSearch = async (contentId: string) => {
    await axios
      .get<CommonResponse<KakaoDocument>>(`/api/v1/map/contents/${contentId}`)
      .then((response) => {
        const document: Document[] = response.data.data.documents
        console.log(document)
      })
      .catch(() => {
        toast.warning('잘못된 입력입니다')
      })
  }

  return {
    kakaoMapProps,
    locationSearch,
    contentSearch,
    contentDetailSearch
  }
})
