<!-- src/views/BoardDetailView.vue -->
<template>
  <!-- boardData가 준비된 후 DetailBoard에 전달 -->
  <DetailNotice
    v-if="boardData"
    :board-data="boardData"
  />
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'
import type { CommonResponse } from '@/service/common'
import DetailNotice from '@/components/notice/DetailNotice.vue'

// 1) useRoute() 로 라우트 가져오기
const route = useRoute()

// 2) route.params만 부분 캐스트
//    bno가 문자열로 들어온다는 것만 TS에 알려줍니다.
const { bno: bnoParam } = route.params as { bno: string }

// 3) 숫자로 변환
const bno = Number(bnoParam)

// 4) 받아올 상세 데이터 타입 정의 (예시)
type BoardDetail = {
  bno: string
  title: string
  content: string
  author: string
  createdDate: string
  views: number
  visibility: string
  boardType: 'board' | 'notice' | 'qna'

  // 필요에 따라 추가 필드...
}

// 5) 데이터를 담을 ref
const boardData = ref<BoardDetail|null>(null)

// 6) 마운트 시 API 호출
onMounted(async () => {
  try {
    const res = await axios.get<CommonResponse<BoardDetail>>(
      `/api/v1/board/${bno}`
    )
    boardData.value = res.data.data
  } catch (err) {
    console.error('상세 조회 실패', err)
    // 에러 처리 로직 추가...
  }
})
</script>
