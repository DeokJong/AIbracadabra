<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { onMounted, reactive } from 'vue'
import axios from 'axios'
import { CommonResponse } from '@/service/common'
import type { BoardSummary } from '@/hooks/boardSummary'
import { useAuth } from '@/hooks/useAuth'
import WithdrawModal from '@/components/modal/BoardDeleteModal.vue'
import { useModal } from '@/hooks/useModal'


const auth = useAuth()
const router = useRouter()
const route = useRoute()
const { addModal } = useModal()

type BoardDetail = BoardSummary & {
  boardType: string
  content: string
  updatedDate: string
  visibility: string
  comments: Comment[]
}

type Comment = {
  cno: number,
  bno: number,
  author: string
  content: string
  createdDate: string
  updatedDate: string
}

const boardData = reactive<BoardDetail>({} as BoardDetail)
const props = defineProps<{ bno: string }>()
const detailApiUrl = `/api/v1/board/${props.bno}`
onMounted(async () => {

  try {
    const response = await axios.get<CommonResponse<BoardDetail>>(detailApiUrl)
    Object.assign(boardData, response.data.data)
  } catch (e) {
    console.error("데이터 불러오기 실패:", e)
  }
})


</script>

<template>
  {{ boardData.author }}

</template>
