<template>
  <v-container class="board-detail-modern py-6">
    <v-row justify="center">
      <v-col cols="12" md="8">
        <!-- 로딩 상태 -->
        <div v-if="isLoading" class="text-center py-8">
          <v-progress-circular indeterminate color="primary"></v-progress-circular>
          <p class="mt-4">게시글을 불러오는 중...</p>
        </div>

        <!-- 게시글 내용 -->
        <template v-else-if="boardData.bno">
          <!-- 타입 컬러 바 + 헤더 -->
          <div class="type-bar" :class="boardData.boardType"></div>
          <v-sheet elevation="3" class="pa-6 board-sheet">
            <div class="header-wrap mb-4">
              <div class="title">{{ boardData.title || '제목 없음' }}</div>
              <div class="meta">
                <span>{{ boardData.author || '작성자 없음' }}</span>
                <span class="dot">·</span>
                <span>{{ formattedDate }}</span>
                <span class="dot">·</span>
                <span>조회 {{ boardData.views || 0 }}</span>
              </div>
              <div
                class="header-actions"
                v-if="canEdit"
              >
                <v-btn small text @click="onRowClick">수정</v-btn>
                <v-btn small text color="error" @click="openWithdrawModal">삭제</v-btn>
              </div>
            </div>  

            <v-divider class="mb-6" />

            <!-- 이미지 갤러리 -->
            <div v-if="hasImages" class="image-gallery mb-8">
              <v-row>
                <v-col
                  v-for="imgNo in boardData.imageUrls"
                  :key="imgNo"
                  cols="12" sm="6" md="4"
                  class="pb-4"
                >
                  <img
                    :src="`/api/v1/board/images/${imgNo}`"
                    alt="Board Image"
                    class="uploaded-image"
                  />
                </v-col>
              </v-row>
            </div>

            <!-- 본문 -->
            <div class="content mb-8">
              {{ boardData.content || '내용이 없습니다.' }}
            </div>
          </v-sheet>

          <v-divider class="mb-4" />

          <!-- 댓글 리스트 -->
          <div v-if="hasComments">
            <div class="comments-header mb-2">
              댓글 ({{ commentCount }})
            </div>

            <div
              v-for="comment in boardData.comments"
              :key="comment.cno"
              class="comment-bubble mb-4"
            >
              <v-avatar size="32" class="mr-2">
                <span>{{ getInitial(comment.author) }}</span>
              </v-avatar>

              <div class="bubble-body">
                <!-- 보기 모드 -->
                <div v-if="editingCommentId !== comment.cno">
                  <div class="bubble-meta">
                    {{ comment.author }} · {{ formatCommentDate(comment.createdDate) }}
                  </div>
                  <div class="bubble-content mb-2">
                    {{ comment.content }}
                  </div>
                  <div
                    class="comment-actions"
                    v-if="canEditComment(comment)"
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
          <v-row justify="center" class="mt-6">
            <v-btn color="primary" @click="onWriteComment">댓글쓰기</v-btn>
          </v-row>
          <RegistComment
            v-if="showCommentForm"
            :bno="boardData.bno"
            @submitted="onCommentSubmit"
            @cancelled="showCommentForm = false"
          />
        </template>

        <!-- 에러 상태 -->
        <div v-else class="text-center py-8">
          <v-icon size="64" color="error">mdi-alert-circle</v-icon>
          <h3 class="mt-4">게시글을 찾을 수 없습니다</h3>
          <p>삭제되었거나 존재하지 않는 게시글입니다.</p>
          <v-btn color="primary" @click="goToList">목록으로 돌아가기</v-btn>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { onMounted, reactive, ref, computed } from 'vue'
import axios from 'axios'
import { CommonResponse } from '@/service/common'
import { BoardSummary } from '@/components/board/ItemTable.vue'
import { useAuth } from '@/hooks/useAuth'
import WithdrawModal from '@/components/modal/BoardDeleteModal.vue'
import { useModal } from '@/hooks/useModal'
import CommentDeleteModal from '@/components/modal/CommentDeleteModal.vue'
import { useToast } from 'vue-toastification'
import RegistComment from '@/components/comment/RegistComment.vue'

// 타입 정의
type BoardDetail = BoardSummary & {
  boardType: string
  content: string
  updatedDate: string
  visibility: string
  comments: Comment[]
  imageUrls: number[]
}

type Comment = {
  cno: number
  bno: number
  author: string
  content: string
  createdDate: string
  updatedDate: string
}

// 컴포저블 및 인스턴스
const auth = useAuth()
const router = useRouter()
const route = useRoute()
const { addModal } = useModal()
const toast = useToast()

export enum PostVisibility {
  PUBLIC = 'PUBLIC',
  PRIVATE = 'PRIVATE', 

}
// 반응형 데이터
const boardData = reactive<BoardDetail>({
  bno: 0,
  title: '',
  mno:0,
  author: '',
  createdDate: '',
  views: 0,
  boardType: '',
  content: '',
  updatedDate: '',
  visibility: 'PRIVATE',
  comments: [],
  imageUrls: []
})

const isLoading = ref<boolean>(true)
const showCommentForm = ref<boolean>(false)
const editingCommentId = ref<number | null>(null)
const editingContent = ref<string>('')

// 계산된 속성
const formattedDate = computed(() => {
  if (!boardData.createdDate) return '날짜 정보 없음'
  try {
    const date = new Date(boardData.createdDate)
    return date.toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    })
  } catch {
    return '날짜 형식 오류'
  }
})

const hasImages = computed(() => {
  return Array.isArray(boardData.imageUrls) && boardData.imageUrls.length > 0
})

const hasComments = computed(() => {
  return Array.isArray(boardData.comments) && boardData.comments.length > 0
})

const commentCount = computed(() => {
  return Array.isArray(boardData.comments) ? boardData.comments.length : 0
})

const canEdit = computed(() => {
  return (auth.userInfo?.role === 'admin') || 
         (auth.isLoggined && auth.userInfo?.name === boardData.author)
})

// 유틸리티 함수
const getInitial = (name: string): string => {
  return name ? name.charAt(0).toUpperCase() : 'U'
}

const formatCommentDate = (dateString: string): string => {
  if (!dateString) return ''
  try {
    const date = new Date(dateString)
    return date.toLocaleDateString('ko-KR')
  } catch {
    return ''
  }
}

const canEditComment = (comment: Comment): boolean => {
  return auth.isLoggined && auth.userInfo?.name === comment.author
}

// API 호출 함수
const fetchBoardData = async (): Promise<void> => {
  isLoading.value = true
  try {
    const detailApiUrl = `api/v1${route.path}`
    const response = await axios.get<CommonResponse<BoardDetail>>(detailApiUrl)
    
    const data = response.data.data
    if (data) {
      Object.assign(boardData, data)
    }
  } catch (error) {
    console.error('게시글 로딩 실패:', error)
    toast.error('게시글을 불러오는데 실패했습니다.')
  } finally {
    isLoading.value = false
  }
}

// 이벤트 핸들러
const openWithdrawModal = async (): Promise<void> => {
  try {
    await addModal<void>(WithdrawModal)
  } catch {
    // 모달이 취소된 경우
  }
}

const onDeleteComment = async (comment: Comment): Promise<void> => {
  try {
    await addModal(CommentDeleteModal)
    await axios.delete(`/api/v1/board/${boardData.bno}/comment/${comment.cno}`)
    
    const idx = boardData.comments.findIndex(c => c.cno === comment.cno)
    if (idx !== -1) {
      boardData.comments.splice(idx, 1)
    }
    toast.success('댓글이 삭제되었습니다.')
  } catch {
    // 취소했거나 에러
  }
}

const onRowClick = (): void => {
  router.push({ 
    path: `/${boardData.boardType}/${boardData.bno}/edit`
  })
}

const onWriteComment = (): void => {
  if (!auth.isLoggined) {
    toast.info('로그인이 필요합니다.')
    router.push('/login')
    return
  }
  showCommentForm.value = !showCommentForm.value
}

const startEdit = (comment: Comment): void => {
  editingCommentId.value = comment.cno
  editingContent.value = comment.content
}

const cancelEdit = (): void => {
  editingCommentId.value = null
  editingContent.value = ''
}

const onCommentSubmit = async (content: string): Promise<void> => {
  console.log('🐣 onCommentSubmit!', content)

  try {
    const res = await axios.post<CommonResponse<Comment>>(
      `/api/v1/board/${boardData.bno}/comment`,
      { content }
    )
    const newComment = res.data.data
    if (newComment) {
      boardData.comments.push(newComment)
      toast.success('댓글이 등록되었습니다.')
    }
  } catch {
    toast.error('댓글 등록에 실패했습니다.')
  } finally {
    showCommentForm.value = false
  }
}

const saveEdit = async (comment: Comment): Promise<void> => {
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
    toast.success('댓글이 수정되었습니다.')
  } catch {
    toast.error('댓글 수정에 실패했습니다.')
  } finally {
    cancelEdit()
  }
}

const goToList = (): void => {
  router.push('/board')
}

// 라이프사이클
onMounted(() => {
  fetchBoardData()
})
</script>

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
.type-bar.qna { background: #ff9800; }

/* 본문 카드 */
.board-sheet {
  border-radius: 12px;
}

/* 헤더 스타일 */
.header-wrap .title {
  font-size: 1.75rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
}
.header-wrap .meta {
  color: #777;
  font-size: 0.875rem;
}
.header-wrap .dot {
  margin: 0 4px;
}

.header-actions {
  margin-top: 1rem;
}

/* 본문 */
.content {
  white-space: pre-wrap;
  line-height: 1.7;
  color: #333;
  min-height: 100px;
}

/* 이미지 */
.uploaded-image {
  width: 100%;
  height: auto;
  border-radius: 8px;
  object-fit: cover;
  max-height: 300px;
}

/* 댓글 섹션 */
.comments-header {
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
  margin-left: 8px;
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
  line-height: 1.4;
}

.comment-actions {
  margin-top: 8px;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .header-wrap .title {
    font-size: 1.4rem;
  }
  
  .uploaded-image {
    max-height: 200px;
  }
}
</style>
