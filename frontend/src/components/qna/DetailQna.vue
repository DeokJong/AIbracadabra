<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { onMounted, reactive, ref } from 'vue'
import axios from 'axios'
import { CommonResponse } from '@/service/common'
import { BoardSummary } from './ItemTable.vue'
import { useAuth } from '@/hooks/useAuth'
import WithdrawModal from '@/components/modal/QnaDeleteModal.vue'
import { useModal } from '@/hooks/useModal'
import CommentDeleteModal from '../modal/CommentDeleteModal.vue'
import { useToast } from 'vue-toastification'
import  RegistComment from '@/components/comment/RegistComment.vue'


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
    // const raw = route.params.bno
  const params = route.params as { bno: string }

  const bno = Number(params.bno)
  const detailApiUrl = `api/v1/board/${bno}`
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
const onDeleteComment = async (comment: Comment) => {
  try {
    // 1) 모달 띄우고
    await addModal(CommentDeleteModal)
    // 2) 확인되면 삭제 API 호출
    await axios.delete(`/api/v1/board/${boardData.bno}/comment/${comment.cno}`)
    // 3) 로컬 배열에서 즉시 제거
    const idx = boardData.comments.findIndex(c => c.cno === comment.cno)
    if (idx !== -1) boardData.comments.splice(idx, 1)
  } catch {
    // 취소했거나 에러
  }
}

const onRowClick = () => {

  router.push({ 
    path: `/${boardData.boardType}/${boardData.bno}/edit`
  })
}
const showCommentForm = ref(false)

const onWriteComment = () => {
  if (!auth.isLoggined) {
    useToast().info('로그인이 필요합니다.')
    router.push('/login')
    return
  }
  showCommentForm.value = !showCommentForm.value
}


// **수정 중인 댓글 ID** 및 **임시 컨텐츠**
const editingCommentId = ref<number | null>(null)
const editingContent = ref<string>('')

function startEdit(comment: Comment) {
  editingCommentId.value = comment.cno
  editingContent.value = comment.content
}

function cancelEdit() {
  editingCommentId.value = null
  editingContent.value = ''
}
async function onCommentSubmit(content: string) {
    console.log('🐣 onCommentSubmit!', content)

  try {
    const res = await axios.post<CommonResponse<Comment>>(
      `/api/v1/board/${boardData.bno}/comment`,
      { content }
    )
    const newComment = res.data.data
    // 삭제 로직처럼 splice 로 추가
    boardData.comments.splice(boardData.comments.length, 0, newComment)
      window.location.reload()
  } catch {
    useToast().error('댓글 등록에 실패했습니다.')
  } finally {
    showCommentForm.value = false
  }
}

async function saveEdit(comment: Comment) {
  try {
    await axios.put(
      `/api/v1/board/${boardData.bno}/comment/${comment.cno}`,
      { content: editingContent.value }
    )
    // 로컬 데이터 업데이트
    const idx = boardData.comments.findIndex(c => c.cno === comment.cno)
    if (idx !== -1) {
      boardData.comments[idx].content = editingContent.value
      boardData.comments[idx].updatedDate = new Date().toISOString()
    }
    useToast().success('댓글이 수정되었습니다.')
  } catch {
    useToast().error('댓글 수정에 실패했습니다.')
  } finally {
    cancelEdit()
  }
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
              <span>조회 {{ boardData.views / 2 }}</span>
            </div>
            <div
              class="header-actions"
              v-if="auth.isLoggined && auth.userInfo.name === boardData.author"
            >
              <v-btn small text @click="onRowClick">수정</v-btn>
              <v-btn small text color="error" @click="openWithdrawModal">삭제</v-btn>
            </div>
          </div>

          <v-divider class="mb-6" />

          <!-- 본문 -->
          <div class="content mb-8">
            {{ boardData.content }}
          </div>
        </v-sheet>

        <v-divider class="mb-4" />

        <!-- 댓글 리스트 -->
        <div v-if="boardData.comments?.length !== undefined">
          <div class="comments-header mb-2">
            댓글 ({{ boardData.comments.length }})
          </div>

          <div
            v-for="comment in boardData.comments"
            :key="comment.cno"
            class="comment-bubble mb-4"
          >
            <v-avatar size="32" class="mr-2">
              <span>{{ comment.author.charAt(0) }}</span>
            </v-avatar>

            <div class="bubble-body">
              <!-- 보기 모드 -->
              <div v-if="editingCommentId !== comment.cno">
                <div class="bubble-meta">
                  {{ comment.author }} · {{ comment.createdDate }}
                </div>
                <div class="bubble-content mb-2">
                  {{ comment.content }}
                </div>
                <div
                  class="comment-actions"
                  v-if="auth.isLoggined && auth.userInfo.name === comment.author"
                >
                  <v-btn small text @click="startEdit(comment)">수정</v-btn>
                  <v-btn small text color="error" @click="onDeleteComment(comment)">
                    삭제
                  </v-btn>
                </div>
              </div>

              <!-- 편집 모드 -->
              <div v-else>
                <v-textarea
                  v-model="editingContent"
                  rows="4"
                  outlined
                />
                <v-row class="mt-2" justify="end">
                  <v-btn text @click="cancelEdit">취소</v-btn>
                  <v-btn color="primary" class="ml-2" @click="saveEdit(comment)">
                    저장
                  </v-btn>
                </v-row>
              </div>
            </div>
          </div>
        </div>

        <!-- 댓글 쓰기 버튼 & 폼 -->
        <v-row justify="center" class="mt-6" v-if="auth.userInfo?.role==='admin'">
          <v-btn color="primary" @click="onWriteComment">댓글쓰기</v-btn>
        </v-row>
        <RegistComment
          v-if="showCommentForm"
          :bno="boardData.bno"
          @submitted="onCommentSubmit"
          @cancelled="showCommentForm = false"
        />
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
