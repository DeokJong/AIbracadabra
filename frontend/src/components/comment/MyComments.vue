<template>
  <div class="comments-container">
    <div class="header-section">
      <h2 class="section-title">
        <span class="title-icon">💬</span>
        내가 쓴 댓글
      </h2>
    </div>

    <div class="table-wrapper">
      <table class="comments-table">
        <thead>
          <tr class="table-header">
            <th class="header-cell">게시글 번호</th>
            <th class="header-cell">작성자</th>
            <th class="header-cell">내용</th>
            <th class="header-cell">작성일</th>
            <th class="header-cell">수정일</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="comment in items"
            :key="comment.cno"
            class="comment-row"
            @click="onRowClick(comment)"
          >
            <td class="cell bno-cell">
              <span class="bno-badge">{{ comment.bno }}</span>
            </td>
            <td class="cell author-cell">{{ comment.author }}</td>
            <td class="cell content-cell">
              <div class="content-preview">{{ comment.content }}</div>
            </td>
            <td class="cell date-cell">{{ comment.createdDate }}</td>
            <td class="cell date-cell">
              <span class="updated-date">{{ comment.updatedDate || '-' }}</span>
            </td>
          </tr>
          <tr v-if="!loading && items.length === 0" class="empty-row">
            <td class="empty-cell" colspan="5">
              <div class="empty-state">
                <span class="empty-icon">📝</span>
                <p>작성된 댓글이 없습니다.</p>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="pagination-container">
      <button
        class="pagination-btn prev-btn"
        :disabled="currentPage <= 1 || loading"
        @click="currentPage--"
      >
        <span class="btn-icon">←</span>
        이전
      </button>
      <div class="page-info">
        <span class="current-page">{{ currentPage }}</span>
        <span class="page-separator">/</span>
        <span class="total-pages">{{ pages }}</span>
      </div>
      <button
        class="pagination-btn next-btn"
        :disabled="currentPage >= pages || loading"
        @click="currentPage++"
      >
        다음
        <span class="btn-icon">→</span>
      </button>
    </div>
  </div>
</template>


<script setup lang="ts">
import { useMyComments } from '@/hooks/useMyBoards'
import { useRouter } from 'vue-router'
import type { CommentSummary } from '@/hooks/useMyBoards'

const router = useRouter()
const { items, pages, currentPage, loading } = useMyComments()

function onRowClick(comment: CommentSummary) {
  // 상세 페이지가 /board/:bno 라면:
  router.push(`/board/${comment.bno}`)
  // Q&A 게시판 전용이면: router.push(`/qna/${comment.bno}`)
}
</script>

<style scoped>
.comments-container {
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  border-radius: 20px;
  padding: 32px;
  margin: 16px 0;
}

.header-section {
  margin-bottom: 32px;
  text-align: center;
}

.section-title {
  font-size: 28px;
  font-weight: 700;
  color: #2d3748;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin: 0;
}

.title-icon {
  font-size: 32px;
}

.table-wrapper {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
  margin-bottom: 24px;
}

.comments-table {
  width: 100%;
  border-collapse: collapse;
}

.table-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.header-cell {
  padding: 20px 16px;
  text-align: left;
  color: white;
  font-weight: 600;
  font-size: 14px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  border: none;
}

.comment-row {
  transition: all 0.3s ease;
  border-bottom: 1px solid #f7fafc;
  cursor: pointer;
}

.comment-row:hover {
  background: linear-gradient(90deg, rgba(102, 126, 234, 0.05) 0%, rgba(118, 75, 162, 0.05) 100%);
  transform: translateX(4px);
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.1);
}

.cell {
  padding: 16px;
  border-bottom: 1px solid #e2e8f0;
  transition: all 0.3s ease;
}

.bno-cell {
  width: 120px;
}

.bno-badge {
  background: linear-gradient(45deg, #667eea, #764ba2);
  color: white;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
}

.author-cell {
  width: 120px;
  font-weight: 500;
  color: #4a5568;
}

.content-cell {
  min-width: 200px;
}

.content-preview {
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #2d3748;
  line-height: 1.5;
}

.date-cell {
  width: 140px;
  color: #718096;
  font-size: 14px;
}

.updated-date {
  font-style: italic;
}

.empty-row {
  background: #fafafa;
}

.empty-cell {
  padding: 60px 20px;
  text-align: center;
  border: none;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  color: #a0aec0;
}

.empty-icon {
  font-size: 48px;
  opacity: 0.5;
}

.empty-state p {
  font-size: 18px;
  font-weight: 500;
  margin: 0;
}

.pagination-container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 24px;
  margin-top: 32px;
}

.pagination-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  background: white;
  color: #4a5568;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.pagination-btn:not(:disabled):hover {
  background: linear-gradient(45deg, #667eea, #764ba2);
  color: white;
  border-color: transparent;
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

.pagination-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  background: #f7fafc;
  color: #a0aec0;
}

.btn-icon {
  font-size: 16px;
  transition: transform 0.3s ease;
}

.pagination-btn:hover .btn-icon {
  transform: translateX(2px);
}

.prev-btn:hover .btn-icon {
  transform: translateX(-2px);
}

.page-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  font-weight: 600;
}

.current-page {
  color: #667eea;
  font-size: 18px;
}

.page-separator {
  color: #a0aec0;
}

.total-pages {
  color: #718096;
}
</style>