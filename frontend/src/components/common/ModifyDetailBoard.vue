<template>
  <v-container class="board-edit py-6">
    <v-row justify="center">
      <v-col cols="12" md="8">
        <div v-if="boardData.bno">
          <v-sheet elevation="3" class="pa-6 board-sheet">
            <v-text-field
              label="제목"
              v-model="boardData.title"
              outlined
              dense
            />

            <v-radio-group
              v-model="boardData.visibility"
              row
              class="mt-4"
            >
              <v-radio label="공개" value="PUBLIC" />
              <v-radio label="비공개" value="PRIVATE" />
            </v-radio-group>

            <v-textarea
              label="내용"
              v-model="boardData.content"
              rows="10"
              outlined
              class="mt-4"
            />

            <v-row class="mt-6" justify="end" align="center">
              <v-btn text @click="onCancel">취소</v-btn>
              <v-btn color="primary" class="ml-2" @click="onSubmit">
                저장
              </v-btn>
            </v-row>
          </v-sheet>
        </div>
        <div v-else class="text-center pa-6">
          로딩중...
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { onMounted, reactive } from 'vue'
import axios from 'axios'
import { CommonResponse } from '@/service/common'
import { BoardSummary } from '@/components/board/ItemTable.vue'

type Comment = {
  cno: number
  bno: number
  author: string
  content: string
  createdDate: string
  updatedDate: string
}

type BoardDetail = BoardSummary & {
  boardType: string
  content: string
  updatedDate: string
  visibility: string
  comments: Comment[]
}

// 라우터
const route = useRoute()
const router = useRouter()

const boardData = reactive<BoardDetail>({} as BoardDetail)

onMounted(async () => {
  const { bno } = route.params as { bno: string }
  const id = Number(bno)

  try {
    const res = await axios.get<CommonResponse<BoardDetail>>(
      `/api/v1/board/${id}`
    )
    Object.assign(boardData, res.data.data)
  } catch (err) {
    console.error('상세 조회 실패', err)
  }
})

async function onSubmit() {
  try {
    await axios.put(`/api/v1/board/${boardData.bno}`, {
      title: boardData.title,
      content: boardData.content,
      visibility: boardData.visibility,
    })
    router.push(`/${boardData.boardType}/${boardData.bno}`)
  } catch (err) {
    console.error('수정 실패', err)
  }
}

function onCancel() {
  router.back()
}
</script>

<style scoped>
.board-edit .board-sheet {
  border-radius: 12px;
}
</style>
