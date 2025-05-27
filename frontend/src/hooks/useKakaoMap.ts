import { FullHotPlace } from '@/hooks/useHotPlace'
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
  HOTPLACE = 'HOTPLACE', // 핫 플레이스
  UNDEFINE = 'UNDEFINE',
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
  mno?: number
  temperature?: string
  description?: string
  sunrise?: string
  sunset?: string
}

export type KakaoDocumentMeta = {
  numOfRows: number
  pageNo: number
  totalCount: number
  end: boolean
}

export type KakaoDocument<T> = {
  meta: KakaoDocumentMeta
  documents: T[]
}

export type KakaoMapMarkerPropsWithInfo = KakaoMapMarkerProps & {
  info: { contentId: string }
}

export type SearchContentParams = {
  contentType: ContentType
  lng: number
  lat: number
  pageNo: number
  level: number
}

/**
 * @return{
 * markerProps - 띄우는 마커 정보
    markerMeta - 띄우는 마커들의 메타 데이터
    kakaoMapProps - 현재 추적중인 맵 데이터
    currentContent - 누른 마커의 정보
    lastSearchContentType - 마지막에 컨텐츠들을 조회한 타입
    locationSearch - 위치 기반 검색 함수
    contentSearch - 현재 좌표 기준 컨텐츠 조회 함수
    contentDetailSearch - 컨텐츠id 기반 상세정보 조회 함수
 * }
 */
export const useKakaoMap = defineStore('kakaoMap', () => {
  const toast = useToast()

  const kakaoMapProps = ref<KakaoMapProps>({ ...DEFAULT_KAKAO_MAP_PROPS })
  const traceMapProps = ref<KakaoMapProps>({ ...DEFAULT_KAKAO_MAP_PROPS })
  const markerProps = ref<KakaoMapMarkerPropsWithInfo[]>([])
  const markerMeta = reactive<KakaoDocumentMeta>({
    numOfRows: 0,
    pageNo: 0,
    totalCount: 0,
    end: true,
  } as KakaoDocumentMeta)
  const lastSearchContentParams = ref<SearchContentParams>({} as SearchContentParams)
  const currentContent = reactive<FullDocument>({} as FullDocument)

  /**
   * 검색어로 보고있는 화면을 옮김
   * @param query 검색어
   * @param pageNo 페이지 넘버 (신경 x)
   */
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

  /**
   * 현재 보고 있는 좌표 기준 컨텐츠 타입 호출
   * 호출하면서 마지막으로 호출한 컨텐츠 타입 저장
   * @param contentType 컨텐츠 타입
   * @param pageNo 페이지
   */
  const contentSearch = async (
    contentType: ContentType,
    mapX: number = traceMapProps.value.lng,
    mapY: number = traceMapProps.value.lat,
    level: number = traceMapProps.value.level ?? 3,
    pageNo: number = 1
  ) => {
    await axios
      .get<CommonResponse<KakaoDocument<FullDocument>>>(
        `/api/v1/map/contents?mapX=${mapX}&mapY=${mapY}&contentTypeId=${contentType}&pageNo=${pageNo}&radius=${mapLevelIntoRadius(level)}`
      )
      .then((response) => {
        if (response.data.data.meta.numOfRows) {
          const document: FullDocument[] = response.data.data.documents
          Object.assign(markerMeta, response.data.data.meta)

          // 이전 검색 기록 저장
          lastSearchContentParams.value.contentType =
            document.length ? ContentCodeResolver(document[0].contentsTypeId) : ContentType.UNDEFINE
          lastSearchContentParams.value.lat =
            lastSearchContentParams.value.lat ?? traceMapProps.value.lat
          lastSearchContentParams.value.lng =
            lastSearchContentParams.value.lng ?? traceMapProps.value.lng
          lastSearchContentParams.value.level =
            lastSearchContentParams.value.level ?? traceMapProps.value.level

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
          toast.info('조회된 데이터가 없습니다')
        }
      })
      .catch(() => {
        toast.warning('잘못된 입력입니다')
      })
  }

  /**
   * 컨텐츠 ID 기반 상세 정보 조회
   * @param contentId
   */
  const contentDetailSearch = async (contentId: string) => {
    if (contentId.startsWith('hotplace_') || contentId.startsWith('hno_')) {
      axios.get<CommonResponse<FullHotPlace>>(`/api/v1/hotPlace/${contentId.replace('hotplace_', '').replace('hno_', '')}`)
        .then((response) => {
          const hotPlace: FullHotPlace = response.data.data
          const document: FullDocument = {
            mapX: hotPlace.mapX,
            mapY: hotPlace.mapY,
            address: "...",
            title: hotPlace.title,
            firstImage: hotPlace.imageUrl,
            contentsTypeId: '40', // 핫 플레이스는 40으로 고정
            contentId: `hotplace_${hotPlace.hno}`,
            overview: hotPlace.overview,
            mno : hotPlace.mno,
          }
          Object.assign(currentContent, document)
        })
        .catch(() => {
          toast.warning('잘못된 입력입니다')
        })
    } else {
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
  }

  function setCurrentContent(item: FullDocument) {
    Object.assign(currentContent, item)
  }

  return {
    markerProps,
    markerMeta,
    kakaoMapProps,
    currentContent,
    lastSearchContentParams,
    traceMapProps,
    locationSearch,
    contentSearch,
    contentDetailSearch,
    setCurrentContent,
  }
})

/**
 * 컨텐츠 Type code로 마커 이미지 불러오는 함수
 */
export const ContentTypeImageResolver = (code: string, type: string = 'png'): string => {
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
    case '40':
      return `${ContentType.HOTPLACE}.${type}` // 핫 플레이스는 40으로 고정
    default:
      // 알 수 없는 코드일 경우 기본 이미지
      return `UNKNOWN.${type}`
  }
}

export const ContentTypeFabIconResolver = (code: string): string => {
  switch (code) {
    case '12':
      return "mdi-map-marker-radius"
    case '14':
      return "mdi-theater"
    case '15':
      return "mdi-party-popper"
    case '25':
      return "mdi-map"
    case '28':
      return "mdi-ski"
    case '32':
      return "mdi-home"
    case '38':
      return "mdi-shopping"
    case '39':
      return "mdi-silverware-fork-knife"
    case '40':
      return "mdi-fire" // 핫 플레이스는 40으로 고정
    default:
      // 알 수 없는 코드일 경우 기본 이미지
      return `UNKNOWN`
  }
}

/**
 * 컨텐츠 타입 코드로 실제 컨텐츠 타입 불러오는 함수
 * @param code
 * @returns
 */
export const ContentCodeResolver = (code: string) => {
  switch (code) {
    case '12':
      return ContentType.TOURIST_SPOT
    case '14':
      return ContentType.CULTURE_FACILITY
    case '15':
      return ContentType.FESTIVAL_EVENT
    case '25':
      return ContentType.TRAVEL_COURSE
    case '28':
      return ContentType.LEISURE_SPORTS
    case '32':
      return ContentType.ACCOMMODATION
    case '38':
      return ContentType.SHOPPING
    case '39':
      return ContentType.RESTAURANT
    case '40':
      return ContentType.HOTPLACE // 핫 플레이스는 40으로 고정
    default:
      // 알 수 없는 코드일 경우 기본 이미지
      return ContentType.UNDEFINE
  }
}

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
