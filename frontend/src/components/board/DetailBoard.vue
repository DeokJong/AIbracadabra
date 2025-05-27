<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { onMounted, reactive, ref } from 'vue'
import axios from 'axios'
import { CommonResponse } from '@/service/common'
import { BoardSummary } from '@/components/board/ItemTable.vue'
import { useAuth } from '@/hooks/useAuth'
import WithdrawModal from '@/components/modal/BoardDeleteModal.vue'
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
  imageUrls: number[]
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
  <v-container class="board-detail-modern py-6">
    <v-row justify="center">
      <v-col cols="12" lg="10" xl="8">
        <!-- 상단 네비게이션 -->
        <div class="breadcrumb-nav mb-4">
          <v-btn 
            variant="text" 
            prepend-icon="mdi-arrow-left"
            class="back-btn"
            @click="router.push('/board')"
          >
            목록으로
          </v-btn>
        </div>

        <!-- 메인 게시글 카드 -->
        <v-card class="main-post-card" elevation="2">
          <!-- 타입 컬러 바 -->
          <div class="type-indicator" :class="boardData.boardType"></div>
          
          <!-- 헤더 섹션 -->
          <v-card-text class="post-header">
            <div class="title-section">
              <h1 class="post-title">{{ boardData.title }}</h1>
              <div class="post-meta">
                <v-avatar size="36" class="author-avatar">
                  <span>{{ boardData.author?.charAt(0) || 'U' }}</span>
                </v-avatar>
                <div class="meta-info">
                  <span class="author-name">{{ boardData.author }}</span>
                  <div class="meta-details">
                    <span class="date">{{ boardData.createdDate }}</span>
                    <span class="divider">•</span>
                    <span class="views">조회 {{ boardData.views }}</span>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- 액션 버튼들 -->
            <div 
              class="action-buttons"
              v-if="auth.userInfo?.role==='admin'||auth.isLoggined && auth.userInfo.name === boardData.author"
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

          <!-- 이미지 갤러리 -->
          <div v-if="boardData.imageUrls?.length" class="image-gallery">
            <div class="gallery-container">
              <div
                v-for="imgNo in boardData.imageUrls"
                :key="imgNo"
                class="image-wrapper"
              >
                <img
                  :src="`/api/v1/board/images/${imgNo}`"
                  alt="Board Image"
                  class="gallery-image"
                  loading="lazy"
                />
              </div>
            </div>
          </div>

          <!-- 본문 내용 -->
          <v-card-text class="post-content">
            <div class="content-body">
              {{ boardData.content }}
            </div>
          </v-card-text>
        </v-card>

        <!-- 댓글 섹션 -->
        <div class="comments-section">
          <div v-if="boardData.comments?.length !== undefined">
            <div class="comments-header">
              <h3 class="comments-title">
                <v-icon class="mr-2">mdi-comment-multiple</v-icon>
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
.board-detail-modern {
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

/* 메인 게시글 카드 */
.main-post-card {
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
.type-indicator.board { background: linear-gradient(90deg, #4caf50, #66bb6a); }
.type-indicator.notice { background: linear-gradient(90deg, #2196f3, #42a5f5); }
.type-indicator.qna { background: linear-gradient(90deg, #ff9800, #ffb74d); }

/* 헤더 섹션 */
.post-header {
  padding: 2rem !important;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 1rem;
}

.title-section {
  flex: 1;
}

.post-title {
  font-size: 2rem;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 1.5rem;
  line-height: 1.3;
}

.post-meta {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.author-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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

/* 이미지 갤러리 */
.image-gallery {
  padding: 1rem 2rem;
}

.gallery-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1rem;
}

.image-wrapper {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.gallery-image {
  width: 100%;
  height: 250px;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.gallery-image:hover {
  transform: scale(1.02);
}

/* 본문 내용 */
.post-content {
  padding: 2rem !important;
  padding-top: 1rem !important;
}

.content-body {
  font-size: 1.1rem;
  line-height: 1.8;
  color: #495057;
  white-space: pre-wrap;
  min-height: 100px;
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
  color: #2c3e50;
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
}

.comment-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-1px);
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
  background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
  color: #8b4513;
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
  color: #2c3e50;
  font-size: 0.95rem;
}

.comment-date {
  font-size: 0.8rem;
  color: #6c757d;
}

.comment-actions {
  display: flex;
  gap: 0.25rem;
}

.comment-body {
  font-size: 0.95rem;
  line-height: 1.6;
  color: #495057;
  white-space: pre-wrap;
  padding-left: 3rem;
}

.comment-edit {
  padding-left: 3rem;
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
}

.comment-form-wrapper {
  margin-top: 1rem;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .post-header {
    flex-direction: column;
    align-items: stretch;
    gap: 1.5rem;
  }

  .action-buttons {
    justify-content: flex-end;
  }

  .post-title {
    font-size: 1.5rem;
  }

  .gallery-container {
    grid-template-columns: 1fr;
  }

  .gallery-image {
    height: 200px;
  }

  .comment-body,
  .comment-edit {
    padding-left: 0;
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
  .post-content,
  .post-header {
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
}

/* 다크모드 지원 */
@media (prefers-color-scheme: dark) {
  .board-detail-modern {
    background: #121212;
  }
  
  .main-post-card {
    background: #1e1e1e;
  }
  
  .post-title,
  .author-name,
  .comments-title,
  .comment-author {
    color: #ffffff;
  }
  
  .content-body,
  .comment-body {
    color: #e0e0e0;
  }
  
  .meta-details,
  .comment-date {
    color: #9e9e9e;
  }
}
</style>
