// src/hooks/useMyBoards.ts
import { ref, watch } from 'vue'
import axios from 'axios'

/**
 * 서버에서 반환하는 PageInfo 구조 (PageHelper 사용 기준)
 */
interface PageInfo<T> {
  list: T[]
  pageNum: number    // 현재 페이지
  pageSize: number   // 한 페이지당 아이템 수
  pages: number      // 전체 페이지 수
  total: number      // 전체 아이템 수
}

/**
 * ItemTable.vue가 기대하는 요약 타입
 */
export interface BoardSummary {
  bno: number
  mno: number
  boardType: string
  visibility: 'PUBLIC' | 'PRIVATE'
  title: string
  author: string
  createdDate: string
  views: number
}

export interface CommentSummary {
    cno: number
    bno: number
    author: string
    content: string
    createdDate: string
    updatedDate: string
}

export function useMyBoards(initialPage = 1) {
  const items = ref<BoardSummary[]>([])
  const currentPage = ref(initialPage)
  const pages = ref(0)
  const loading = ref(false)
  const pageSize = 20  // backend DEFAULT_PAGE_SIZE 과 동일

  // 실제 API 호출
  const fetchPage = async (page: number) => {
    loading.value = true
    try {
      const res = await axios.get<PageInfo<BoardSummary>>(
        '/api/v1/board/member',
        { params: { currentPage: page } }
      )
      items.value = res.data.list
      pages.value = res.data.pages
      // 서버가 응답해주는 현재 페이지 번호
      currentPage.value = res.data.pageNum
    } finally {
      loading.value = false
    }
  }

  // 초기 로드
  fetchPage(initialPage)

  // currentPage가 바뀌면 다시 호출
  watch(currentPage, (newPage, _) => {
    fetchPage(newPage)
  })

  return {
    items,
    currentPage,
    pages,
    loading,
  }
}

export function useMyComments(initialPage = 1) {
  const items = ref<CommentSummary[]>([])
  const currentPage = ref(initialPage)
  const pages = ref(0)
  const loading = ref(false)
  const pageSize = 20  // backend DEFAULT_PAGE_SIZE 과 동일

  // 실제 API 호출
  const fetchPage = async (page: number) => {
    loading.value = true
    try {
      const res = await axios.get<PageInfo<CommentSummary>>(
        '/api/v1/board/comment',
        { params: { currentPage: page } }
      )
      items.value = res.data.list
      pages.value = res.data.pages
      // 서버가 응답해주는 현재 페이지 번호
      currentPage.value = res.data.pageNum
    } finally {
      loading.value = false
    }
  }

  // 초기 로드
  fetchPage(initialPage)

  // currentPage가 바뀌면 다시 호출
  watch(currentPage, (newPage, _) => {
    fetchPage(newPage)
  })

  return {
    items,
    currentPage,
    pages,
    loading,
  }
}