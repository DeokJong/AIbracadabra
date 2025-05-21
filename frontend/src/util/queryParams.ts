// src/utils/queryParams.ts
import type { LocationQuery } from 'vue-router'

/**
 * 주어진 query 객체를 순회하며
 * URLSearchParams 에 키/값을 append 해 줍니다.
 */
export function appendQueryParams(
  params: URLSearchParams,
  query: LocationQuery
): void {
  Object.entries(query).forEach(([key, val]) => {
    if (typeof val === 'string') {
      params.append(key, val)
    }
    else if (Array.isArray(val)) {
      val.forEach(v => {
        if (typeof v === 'string') {
          params.append(key, v)
        }
      })
    }
    // null, undefined 등은 건너뜀
  })
}

/**
 * query 로부터 바로 쿼리스트링(`a=1&b=2`) 만 반환하고 싶다면
 */
export function buildQueryString(query: LocationQuery): string {
  const params = new URLSearchParams()
  appendQueryParams(params, query)
  return params.toString()
}
