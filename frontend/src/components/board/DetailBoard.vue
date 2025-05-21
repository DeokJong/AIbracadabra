<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { onMounted, reactive } from 'vue'
import axios from 'axios'
import { CommonResponse } from '@/service/common'
import { BoardSummary } from './ItemTable.vue'
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

onMounted(async () => {
  const detailApiUrl = `api/v1${route.path}`
  const response = await axios.get<CommonResponse<BoardDetail>>(detailApiUrl)
  // TODO 니가 결과값에 맞춰서 바꾸어라
  // 그런데 내가 지금 페이지 네이션 관련해서 다루고 있으니 일단은 남겨라
  console.log(response.data.data)
  Object.assign(boardData, response.data.data)
})

const openWithdrawModal = async () => {
  try {
    await addModal<void>(WithdrawModal)
  } catch {
  }
}

const onRowClick = () => {

  router.push({ 
    path: `/board/${boardData.bno}/edit`
  })
}


</script>

<template>
  <v-container class="board-detail-modern py-6">
    <v-row justify="center">
      <v-col cols="12" md="8">
        <!-- 타입 컬러 바 + 헤더 -->
        <div class="type-bar" :class="boardData.boardType"></div>
        <v-sheet elevation="3" class="pa-6 board-sheet">
          <div class="header-wrap mb-4">
            <div class="title">{{ boardData.title }}</div>
            <div class="meta">
              <span>{{ boardData.author }}</span>
              <span class="dot">·</span>
              <span>{{ boardData.createdDate }}</span>
              <span class="dot">·</span>
              <span>조회 {{ boardData.views }}</span>
            </div>
            <!-- 오른쪽: 수정/삭제 버튼 -->
            <div class="header-actions" v-if="auth.isLoggined && auth.userInfo.name === boardData.author">
              <v-btn
                small
                text
                @click="onRowClick"

              >
                수정
              </v-btn>
              <v-btn
                small
                text
                color="error"
                @click="openWithdrawModal"
              >
                삭제
              </v-btn>
            </div>

          </div>


          <v-divider class="mb-6"></v-divider>

          <!-- 본문 -->
          <div class="content mb-8">
            {{ boardData.content }}
          </div>

          <!-- 댓글 리스트 -->
          <v-divider class="mb-4"></v-divider>
          <div class="comments-wrap" v-if="boardData.comments && boardData.comments.length >= 0"  >
            <div class="comments-header">
              댓글 ({{ boardData.comments.length }})
            </div>
            <div v-for="comment in boardData.comments" :key="comment.cno" class="comment-bubble mb-3">
              <v-avatar size="32" class="mr-2">
                <span>{{ comment.author.charAt(0) }}</span>
              </v-avatar>
              <div class="bubble-body">
                <div class="bubble-meta">{{ comment.author }} · {{ comment.createdDate }}</div>
                <div class="bubble-content">{{ comment.content }}</div>
              </div>
            </div>
          </div>
        </v-sheet>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.board-detail-modern {
  max-width: 100%;
}

/* 게시판 타입별 컬러 바 */
.type-bar {
  height: 4px;
  margin-bottom: -4px;
}
.type-bar.board { background: #4caf50; }
.type-bar.notice { background: #2196f3; }
.type-bar.qna   { background: #ff9800; }

/* 본문 카드 */
.board-sheet {
  border-radius: 12px;
}

/* 헤더 스타일 */
.header-wrap .title {
  font-size: 1.75rem;
  font-weight: 700;
}
.header-wrap .meta {
  color: #777;
  font-size: 0.875rem;
}
.header-wrap .dot {
  margin: 0 4px;
}

/* 본문 */
.content {
  white-space: pre-wrap;
  line-height: 1.7;
  color: #333;
}

/* 댓글 섹션 */
.comments-wrap .comments-header {
  font-size: 1.125rem;
  font-weight: 500;
  margin-bottom: 12px;
}

/* 댓글 버블 */
.comment-bubble {
  display: flex;
  align-items: flex-start;
}

.bubble-body {
  background: #f5f5f5;
  border-radius: 16px;
  padding: 12px 16px;
  flex: 1;
}

.bubble-meta {
  font-size: 0.75rem;
  color: #999;
  margin-bottom: 4px;
}

.bubble-content {
  font-size: 0.95rem;
  color: #444;
  white-space: pre-wrap;
}
</style>
