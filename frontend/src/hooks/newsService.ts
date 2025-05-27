import axios from 'axios'

export interface News {
  id: number
  title: string
  publishAt: string
  sidoCode: number
  summary: string
  url: string
}

/** 모든 뉴스 가져오기 */
export async function fetchAllNews(): Promise<News[]> {
  const res = await axios.get<News[]>('/api/v1/news')
  return res.data
}

/** 시도 코드별 뉴스 가져오기 */
export async function fetchNewsBySidoCode(sidoCode: number): Promise<News[]> {
  const res = await axios.get<News[]>(`/api/v1/news/${sidoCode}`)
  return res.data
}
