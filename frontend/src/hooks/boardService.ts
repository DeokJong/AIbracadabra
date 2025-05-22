// src/components/board/boardService.ts
import axios from 'axios'

export interface Board {
  bno: number
  title: string
  content: string
  author: string
  createdDate: string
  views: number
}

/**
 * boardType 에 따라 인기 글 5개 가져오기
 * - board   : 일반 게시판
 * - notice  : 공지사항
 * - qna     : Q&A
 */
export async function fetchPopularBoards(
  boardType: 'board' | 'notice' | 'qna'
): Promise<Board[]> {
  const res = await axios.get<Board[]>(
    `/api/v1/board/views/${encodeURIComponent(boardType)}`
  )
  return res.data
}
