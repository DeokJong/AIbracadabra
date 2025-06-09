<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { onMounted, reactive, ref } from 'vue'
import axios from 'axios'
import { CommonResponse } from '@/service/common'
import { BoardSummary } from '@/components/board/ItemTable.vue'
import { useAuth } from '@/hooks/useAuth'
import WithdrawModal from '@/components/modal/NoticeDeleteModal.vue'
import { useModal } from '@/hooks/useModal'
import CommentDeleteModal from '@/components/modal/CommentDeleteModal.vue'
import { useToast } from 'vue-toastification'
import RegistComment from '@/components/comment/RegistComment.vue'

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
const comments = ref<Comment[]>([])

onMounted(async () => {
  const params = route.params as { bno: string }
  const bno = Number(params.bno)
  const detailApiUrl = `/api/v1/board/${bno}`
  const response = await axios.get<CommonResponse<BoardDetail>>(detailApiUrl)
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
    await addModal(CommentDeleteModal)
    await axios.delete(`/api/v1/board/${boardData.bno}/comment/${comment.cno}`)
    const idx = boardData.comments.findIndex(c => c.cno === comment.cno)
    if (idx !== -1) boardData.comments.splice(idx, 1)
  } catch {
  }
}

const editingCommentId = ref<number | null>(null)
const editingContent = ref<string>('')

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

async function onCommentSubmit(content: string) {
  console.log('🐣 onCommentSubmit!', content)
  try {
    const res = await axios.post<CommonResponse<Comment>>(
      `/api/v1/board/${boardData.bno}/comment`,
      { content }
    )
    const newComment = res.data.data
    boardData.comments.splice(boardData.comments.length, 0, newComment)
    window.location.reload()
  } catch {
    useToast().error('댓글 등록에 실패했습니다.')
  } finally {
    showCommentForm.value = false
  }
}

function startEdit(comment: Comment) {
  editingCommentId.value = comment.cno
  editingContent.value = comment.content
}

function cancelEdit() {
  editingCommentId.value = null
  editingContent.value = ''
}

async function saveEdit(comment: Comment) {
  try {
    await axios.put(
      `/api/v1/board/${boardData.bno}/comment/${comment.cno}`,
      { content: editingContent.value }
    )
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
  <v-container class="notice-detail-modern py-6">
    <v-row justify="center">
      <v-col cols="12" lg="10" xl="8">
        <!-- 상단 네비게이션 -->
        <div class="breadcrumb-nav mb-4">
          <v-btn
            variant="text"
            prepend-icon="mdi-arrow-left"
            class="back-btn"
            @click="router.push('/notice')"
          >
            공지사항 목록으로
          </v-btn>
        </div>

        <!-- 메인 공지사항 카드 -->
        <v-card class="main-notice-card" elevation="2">
          <!-- 타입 컬러 바 -->
          <div class="type-indicator notice"></div>

          <!-- 헤더 섹션 -->
          <v-card-text class="notice-header">
            <div class="title-section">
              <div class="notice-badge">
                <v-icon size="20">mdi-bullhorn</v-icon>
                공지사항
              </div>
              <h1 class="notice-title">{{ boardData.title }}</h1>
              <div class="notice-meta">
                <v-avatar size="36" class="admin-avatar">
                  <v-icon color="white">mdi-account-star</v-icon>
                </v-avatar>
                <div class="meta-info">
                  <span class="author-name">{{ boardData.author || '관리자' }}</span>
                  <div class="meta-details">
                    <span class="date">{{ boardData.createdDate }}</span>
                    <span class="divider">•</span>
                    <span class="views">조회 {{ Math.floor((boardData.views || 0) / 2) }}</span>
                  </div>
                </div>
              </div>
            </div>

            <!-- 액션 버튼들 (관리자만) -->
            <div
              class="action-buttons"
              v-if="auth.userInfo?.role === 'admin'"
            >
              <v-btn
                variant="outlined"
                color="primary"
                size="small"
                prepend-icon="mdi-pencil"
                @click="onRowClick"
              >
                수정
              </v-btn>
              <v-btn
                variant="outlined"
                color="error"
                size="small"
                prepend-icon="mdi-delete"
                @click="openWithdrawModal"
              >
                삭제
              </v-btn>
            </div>
          </v-card-text>

          <v-divider></v-divider>

          <!-- 본문 내용 -->
          <v-card-text class="notice-content">
            <div class="content-body">
              <div class="important-notice">
                <v-icon class="mr-2" color="orange">mdi-alert</v-icon>
                중요한 공지사항입니다. 반드시 확인해 주시기 바랍니다.
              </div>
              <div class="notice-text">
                {{ boardData.content }}
              </div>
            </div>
          </v-card-text>
        </v-card>

        <!-- 댓글 섹션 -->
        <div class="comments-section">
          <div v-if="boardData.comments?.length !== undefined">
            <div class="comments-header">
              <h3 class="comments-title">
                <v-icon class="mr-2" color="blue">mdi-comment-multiple</v-icon>
                댓글 {{ boardData.comments.length }}개
              </h3>
            </div>

            <div class="comments-list">
              <v-card
                v-for="comment in boardData.comments"
                :key="comment.cno"
                class="comment-card"
                variant="outlined"
              >
                <v-card-text class="comment-content">
                  <div class="comment-header">
                    <v-avatar size="32" class="comment-avatar">
                      <span>{{ comment.author.charAt(0) }}</span>
                    </v-avatar>
                    <div class="comment-meta">
                      <span class="comment-author">{{ comment.author }}</span>
                      <span class="comment-date">{{ comment.createdDate }}</span>
                    </div>

                    <!-- 댓글 액션 버튼 -->
                    <div
                      class="comment-actions"
                      v-if="auth.isLoggined && auth.userInfo.name === comment.author && editingCommentId !== comment.cno"
                    >
                      <v-btn
                        variant="text"
                        size="small"
                        icon="mdi-pencil"
                        @click="startEdit(comment)"
                      ></v-btn>
                      <v-btn
                        variant="text"
                        size="small"
                        color="error"
                        icon="mdi-delete"
                        @click="onDeleteComment(comment)"
                      ></v-btn>
                    </div>
                  </div>

                  <!-- 댓글 내용 보기/수정 모드 -->
                  <div v-if="editingCommentId !== comment.cno" class="comment-body">
                    {{ comment.content }}
                  </div>

                  <div v-else class="comment-edit">
                    <v-textarea
                      v-model="editingContent"
                      rows="3"
                      variant="outlined"
                      hide-details
                      placeholder="댓글을 수정해주세요..."
                    ></v-textarea>
                    <div class="edit-actions">
                      <v-btn
                        variant="text"
                        size="small"
                        @click="cancelEdit"
                      >
                        취소
                      </v-btn>
                      <v-btn
                        variant="flat"
                        size="small"
                        color="primary"
                        @click="saveEdit(comment)"
                      >
                        저장
                      </v-btn>
                    </div>
                  </div>
                </v-card-text>
              </v-card>
            </div>
          </div>

          <!-- 댓글 작성 버튼 및 폼 -->
          <div class="comment-write-section">
            <v-btn
              color="primary"
              variant="flat"
              prepend-icon="mdi-comment-plus"
              block
              class="write-comment-btn"
              @click="onWriteComment"
            >
              댓글 작성하기
            </v-btn>

            <div v-if="showCommentForm" class="comment-form-wrapper">
              <RegistComment
                :bno="boardData.bno"
                @submitted="onCommentSubmit"
                @cancelled="showCommentForm = false"
              />
            </div>
          </div>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
/* 전체 컨테이너 */
.notice-detail-modern {
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  min-height: 100vh;
}

/* 상단 네비게이션 */
.breadcrumb-nav {
  margin-bottom: 1.5rem;
}

.back-btn {
  color: #6c757d;
  font-weight: 500;
}

/* 메인 공지사항 카드 */
.main-notice-card {
  border-radius: 16px;
  margin-bottom: 2rem;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(33, 150, 243, 0.15) !important;
  border: 1px solid rgba(33, 150, 243, 0.1);
}

/* 타입 인디케이터 */
.type-indicator {
  height: 5px;
  width: 100%;
}
.type-indicator.notice {
  background: linear-gradient(90deg, #2196f3, #1976d2, #0d47a1);
}

/* 공지사항 헤더 섹션 */
.notice-header {
  padding: 2rem !important;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 1rem;
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
}

.title-section {
  flex: 1;
}

.notice-badge {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  background: linear-gradient(135deg, #2196f3, #1976d2);
  color: white;
  padding: 0.6rem 1.2rem;
  border-radius: 25px;
  font-size: 0.875rem;
  font-weight: 600;
  margin-bottom: 1rem;
  box-shadow: 0 4px 12px rgba(33, 150, 243, 0.3);
}

.notice-title {
  font-size: 2.2rem;
  font-weight: 700;
  color: #0d47a1;
  margin-bottom: 1.5rem;
  line-height: 1.3;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.notice-meta {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.admin-avatar {
  background: linear-gradient(135deg, #2196f3 0%, #0d47a1 100%);
  border: 3px solid white;
  box-shadow: 0 2px 8px rgba(33, 150, 243, 0.3);
}

.meta-info {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.author-name {
  font-weight: 600;
  color: #0d47a1;
  font-size: 1rem;
}

.meta-details {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
  color: #1565c0;
}

.divider {
  color: #90caf9;
}

/* 액션 버튼 */
.action-buttons {
  display: flex;
  gap: 0.5rem;
  flex-shrink: 0;
}

/* 본문 내용 */
.notice-content {
  padding: 2rem !important;
  background: white;
}

.content-body {
  font-size: 1.1rem;
  line-height: 1.8;
  color: #37474f;
}

.important-notice {
  background: linear-gradient(135deg, #fff3e0, #ffe0b2);
  border: 1px solid #ffcc02;
  border-left: 4px solid #ff9800;
  padding: 1rem;
  border-radius: 8px;
  margin-bottom: 2rem;
  display: flex;
  align-items: center;
  font-weight: 500;
  color: #e65100;
}

.notice-text {
  white-space: pre-wrap;
  min-height: 150px;
  background: #f8f9fa;
  padding: 2rem;
  border-radius: 12px;
  border-left: 4px solid #2196f3;
  font-size: 1.05rem;
  line-height: 1.7;
}

/* 댓글 섹션 */
.comments-section {
  margin-top: 2rem;
}

.comments-header {
  margin-bottom: 1.5rem;
}

.comments-title {
  display: flex;
  align-items: center;
  font-size: 1.25rem;
  font-weight: 600;
  color: #0d47a1;
  margin: 0;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 2rem;
}

.comment-card {
  border-radius: 12px;
  transition: all 0.2s ease;
  background: white;
  border-left: 3px solid #2196f3;
}

.comment-card:hover {
  box-shadow: 0 4px 12px rgba(33, 150, 243, 0.15);
  transform: translateY(-2px);
}

.comment-content {
  padding: 1.5rem !important;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1rem;
}

.comment-avatar {
  background: linear-gradient(135deg, #90caf9, #42a5f5);
  color: #0d47a1;
  font-weight: 600;
}

.comment-meta {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.comment-author {
  font-weight: 600;
  color: #0d47a1;
  font-size: 0.95rem;
}

.comment-date {
  font-size: 0.8rem;
  color: #1565c0;
}

.comment-actions {
  display: flex;
  gap: 0.25rem;
}

.comment-body {
  font-size: 0.95rem;
  line-height: 1.6;
  color: #37474f;
  white-space: pre-wrap;
  padding-left: 3rem;
  background: #f8f9fa;
  padding: 1rem;
  border-radius: 8px;
  margin-top: 0.5rem;
}

.comment-edit {
  margin-top: 0.5rem;
}

.edit-actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
  margin-top: 1rem;
}

/* 댓글 작성 섹션 */
.comment-write-section {
  margin-top: 2rem;
}

.write-comment-btn {
  border-radius: 12px;
  padding: 1rem;
  font-weight: 600;
  text-transform: none;
  letter-spacing: 0;
  background: linear-gradient(135deg, #2196f3, #1976d2);
}

.comment-form-wrapper {
  margin-top: 1rem;
  background: white;
  padding: 1rem;
  border-radius: 12px;
  border: 1px solid #e3f2fd;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .notice-header {
    flex-direction: column;
    align-items: stretch;
    gap: 1.5rem;
  }

  .action-buttons {
    justify-content: flex-end;
  }

  .notice-title {
    font-size: 1.6rem;
  }

  .comment-body {
    padding-left: 0;
    margin-top: 0.75rem;
  }

  .comment-edit {
    margin-top: 0.75rem;
  }

  .comment-header {
    flex-wrap: wrap;
  }

  .comment-actions {
    margin-left: auto;
  }
}

@media (max-width: 480px) {
  .notice-content,
  .notice-header {
    padding: 1rem !important;
  }

  .comment-content {
    padding: 1rem !important;
  }

  .meta-details {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.25rem;
  }

  .divider {
    display: none;
  }

  .notice-badge {
    font-size: 0.75rem;
    padding: 0.5rem 1rem;
  }

  .important-notice {
    padding: 0.75rem;
    font-size: 0.9rem;
  }
}

/* 다크모드 지원 */
@media (prefers-color-scheme: dark) {
  .notice-detail-modern {
    background: #121212;
  }

  .main-notice-card,
  .comment-card {
    background: #1e1e1e;
  }

  .notice-header {
    background: linear-gradient(135deg, #263238, #37474f);
  }

  .notice-title,
  .author-name,
  .comments-title,
  .comment-author {
    color: #90caf9;
  }

  .notice-text,
  .comment-body {
    color: #e0e0e0;
    background: #2a2a2a;
  }

  .meta-details,
  .comment-date {
    color: #64b5f6;
  }
}
</style>
