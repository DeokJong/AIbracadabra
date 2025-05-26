<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { onMounted, reactive, ref } from 'vue'
import axios from 'axios'
import { CommonResponse } from '@/service/common'
import { BoardSummary } from '@/components/board/ItemTable.vue'
import { useAuth } from '@/hooks/useAuth'
import WithdrawModal from '@/components/modal/QnaDeleteModal.vue'
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

onMounted(async () => {
  const params = route.params as { bno: string }
  const bno = Number(params.bno)
  const detailApiUrl = `api/v1/board/${bno}`
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
  <v-container class="qna-detail-modern py-6">
    <v-row justify="center">
      <v-col cols="12" lg="10" xl="8">
        <!-- 상단 네비게이션 -->
        <div class="breadcrumb-nav mb-4">
          <v-btn 
            variant="text" 
            prepend-icon="mdi-arrow-left"
            class="back-btn"
            @click="router.push('/qna')"
          >
            Q&A 목록으로
          </v-btn>
        </div>

        <!-- 메인 QNA 카드 -->
        <v-card class="main-qna-card" elevation="2">
          <!-- 타입 컬러 바 -->
          <div class="type-indicator qna"></div>
          
          <!-- 헤더 섹션 -->
          <v-card-text class="qna-header">
            <div class="title-section">
              <div class="qna-badge">
                <v-icon size="20">mdi-help-circle</v-icon>
                Q&A
              </div>
              <h1 class="qna-title">{{ boardData.title }}</h1>
              <div class="qna-meta">
                <v-avatar size="36" class="author-avatar">
                  <span>{{ boardData.author?.charAt(0) || 'U' }}</span>
                </v-avatar>
                <div class="meta-info">
                  <span class="author-name">{{ boardData.author }}</span>
                  <div class="meta-details">
                    <span class="date">{{ boardData.createdDate }}</span>
                    <span class="divider">•</span>
                    <span class="views">조회 {{ Math.floor((boardData.views || 0) / 2) }}</span>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- 액션 버튼들 -->
            <div 
              class="action-buttons"
              v-if="auth.isLoggined && auth.userInfo.name === boardData.author"
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
          <v-card-text class="qna-content">
            <div class="content-body">
              {{ boardData.content }}
            </div>
          </v-card-text>
        </v-card>

        <!-- 답변 섹션 -->
        <div class="answers-section">
          <div v-if="boardData.comments?.length !== undefined">
            <div class="answers-header">
              <h3 class="answers-title">
                <v-icon class="mr-2" color="orange">mdi-comment-text-multiple</v-icon>
                답변 {{ boardData.comments.length }}개
              </h3>
            </div>

            <div class="answers-list">
              <v-card
                v-for="comment in boardData.comments"
                :key="comment.cno"
                class="answer-card"
                variant="outlined"
              >
                <v-card-text class="answer-content">
                  <div class="answer-header">
                    <div class="answer-badge">
                      <v-icon size="16">mdi-comment-text</v-icon>
                      답변
                    </div>
                    <v-avatar size="32" class="answer-avatar">
                      <span>{{ comment.author.charAt(0) }}</span>
                    </v-avatar>
                    <div class="answer-meta">
                      <span class="answer-author">{{ comment.author }}</span>
                      <span class="answer-date">{{ comment.createdDate }}</span>
                    </div>
                    
                    <!-- 답변 액션 버튼 -->
                    <div
                      class="answer-actions"
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

                  <!-- 답변 내용 보기/수정 모드 -->
                  <div v-if="editingCommentId !== comment.cno" class="answer-body">
                    {{ comment.content }}
                  </div>

                  <div v-else class="answer-edit">
                    <v-textarea
                      v-model="editingContent"
                      rows="4"
                      variant="outlined"
                      hide-details
                      placeholder="답변을 수정해주세요..."
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

          <!-- 답변 작성 버튼 및 폼 (관리자만) -->
          <div class="answer-write-section" v-if="auth.userInfo?.role==='admin'">
            <v-btn
              color="orange"
              variant="flat"
              prepend-icon="mdi-comment-plus"
              block
              class="write-answer-btn"
              @click="onWriteComment"
            >
              답변 작성하기
            </v-btn>
            
            <div v-if="showCommentForm" class="answer-form-wrapper">
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
.qna-detail-modern {
  background: #f8f9fa;
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

/* 메인 QNA 카드 */
.main-qna-card {
  border-radius: 16px;
  margin-bottom: 2rem;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08) !important;
}

/* 타입 인디케이터 */
.type-indicator {
  height: 4px;
  width: 100%;
}
.type-indicator.qna { 
  background: linear-gradient(90deg, #ff9800, #ffb74d); 
}

/* QNA 헤더 섹션 */
.qna-header {
  padding: 2rem !important;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 1rem;
}

.title-section {
  flex: 1;
}

.qna-badge {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  background: linear-gradient(135deg, #ff9800, #ffb74d);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.875rem;
  font-weight: 600;
  margin-bottom: 1rem;
}

.qna-title {
  font-size: 2rem;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 1.5rem;
  line-height: 1.3;
}

.qna-meta {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.author-avatar {
  background: linear-gradient(135deg, #ff9800 0%, #f57c00 100%);
  color: white;
  font-weight: 600;
}

.meta-info {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.author-name {
  font-weight: 600;
  color: #2c3e50;
  font-size: 1rem;
}

.meta-details {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
  color: #6c757d;
}

.divider {
  color: #dee2e6;
}

/* 액션 버튼 */
.action-buttons {
  display: flex;
  gap: 0.5rem;
  flex-shrink: 0;
}

/* 본문 내용 */
.qna-content {
  padding: 2rem !important;
  padding-top: 1rem !important;
}

.content-body {
  font-size: 1.1rem;
  line-height: 1.8;
  color: #495057;
  white-space: pre-wrap;
  min-height: 100px;
  background: #f8f9fa;
  padding: 1.5rem;
  border-radius: 12px;
  border-left: 4px solid #ff9800;
}

/* 답변 섹션 */
.answers-section {
  margin-top: 2rem;
}

.answers-header {
  margin-bottom: 1.5rem;
}

.answers-title {
  display: flex;
  align-items: center;
  font-size: 1.25rem;
  font-weight: 600;
  color: #2c3e50;
  margin: 0;
}

.answers-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 2rem;
}

.answer-card {
  border-radius: 12px;
  transition: all 0.2s ease;
  border-left: 4px solid #4caf50;
}

.answer-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-1px);
}

.answer-content {
  padding: 1.5rem !important;
}

.answer-header {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1rem;
}

.answer-badge {
  display: inline-flex;
  align-items: center;
  gap: 0.25rem;
  background: linear-gradient(135deg, #4caf50, #66bb6a);
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
}

.answer-avatar {
  background: linear-gradient(135deg, #4caf50 0%, #388e3c 100%);
  color: white;
  font-weight: 600;
}

.answer-meta {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.answer-author {
  font-weight: 600;
  color: #2c3e50;
  font-size: 0.95rem;
}

.answer-date {
  font-size: 0.8rem;
  color: #6c757d;
}

.answer-actions {
  display: flex;
  gap: 0.25rem;
}

.answer-body {
  font-size: 0.95rem;
  line-height: 1.6;
  color: #495057;
  white-space: pre-wrap;
  padding-left: 3rem;
  background: #f8f9fa;
  padding: 1rem;
  border-radius: 8px;
  margin-top: 0.5rem;
}

.answer-edit {
  margin-top: 0.5rem;
}

.edit-actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
  margin-top: 1rem;
}

/* 답변 작성 섹션 */
.answer-write-section {
  margin-top: 2rem;
}

.write-answer-btn {
  border-radius: 12px;
  padding: 1rem;
  font-weight: 600;
  text-transform: none;
  letter-spacing: 0;
}

.answer-form-wrapper {
  margin-top: 1rem;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .qna-header {
    flex-direction: column;
    align-items: stretch;
    gap: 1.5rem;
  }

  .action-buttons {
    justify-content: flex-end;
  }

  .qna-title {
    font-size: 1.5rem;
  }

  .answer-body {
    padding-left: 0;
    margin-top: 0.75rem;
  }

  .answer-edit {
    margin-top: 0.75rem;
  }

  .answer-header {
    flex-wrap: wrap;
  }

  .answer-actions {
    margin-left: auto;
  }
}

@media (max-width: 480px) {
  .qna-content,
  .qna-header {
    padding: 1rem !important;
  }

  .answer-content {
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

  .qna-badge,
  .answer-badge {
    font-size: 0.75rem;
    padding: 0.375rem 0.75rem;
  }
}

/* 다크모드 지원 */
@media (prefers-color-scheme: dark) {
  .qna-detail-modern {
    background: #121212;
  }
  
  .main-qna-card,
  .answer-card {
    background: #1e1e1e;
  }
  
  .qna-title,
  .author-name,
  .answers-title,
  .answer-author {
    color: #ffffff;
  }
  
  .content-body,
  .answer-body {
    color: #e0e0e0;
    background: #2a2a2a;
  }
  
  .meta-details,
  .answer-date {
    color: #9e9e9e;
  }
}
</style>
