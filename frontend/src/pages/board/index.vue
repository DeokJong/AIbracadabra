<template>
  <div class="board-list-container">
    <!-- 헤더 -->
    <div class="list-header">
      <h2 class="page-title">게시판</h2>
      <button
        class="write-btn"
        @click="goWrite"
      >
        <i class="fas fa-plus"></i>
        글쓰기
      </button>
    </div>

    <!-- 로딩 상태 -->
    <div v-if="table.loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>게시글을 불러오는 중...</p>
    </div>

    <!-- 게시글 그리드 -->
    <div v-else-if="table.items.length > 0" class="board-grid">
      <BoardItem
        v-for="item in table.items"
        :key="item.bno"
        :item="item"
      />
    </div>

    <!-- 빈 상태 -->
    <div v-else class="empty-state">
      <i class="fas fa-file-text"></i>
      <p>등록된 게시글이 없습니다.</p>
      <button class="write-btn" @click="goWrite">
        첫 번째 글을 작성해보세요
      </button>
    </div>

    <!-- 페이지네이션 -->
    <div v-if="table.pages > 1" class="pagination">
      <button
        v-for="page in paginationRange"
        :key="page"
        :class="['page-btn', { active: page === table.page }]"
        @click="fetchData(page)"
      >
        {{ page }}
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, onMounted, computed } from 'vue'
import axios from 'axios'
import { useRouter, useRoute } from 'vue-router'
import { useAuth } from '@/hooks/useAuth'
import BoardItem from '@/components/board/BoardItem.vue' // 위에서 수정한 컴포넌트
import type { BoardSummary } from '@/hooks/boardSummary'
const auth = useAuth()
const router = useRouter()
const route = useRoute()

const API_URL = '/api/v1/board'

const table = reactive({
  items: [] as BoardSummary[],
  page: Number(route.query.currentPage) || 1,
  pages: 0,
  loading: false,
  itemsPerPage: 20
})

const paginationRange = computed(() => {
  const range = []
  const start = Math.max(1, table.page - 2)
  const end = Math.min(table.pages, table.page + 2)

  for (let i = start; i <= end; i++) {
    range.push(i)
  }
  return range
})

async function fetchData(newPage = table.page) {
  table.loading = true
  try {
    const res = await axios.get(API_URL, {
      params: {
        boardType: 'board',
        currentPage: newPage,
        pageSize: table.itemsPerPage ?? 20
      }
    })
    const payload = res.data.data ?? res.data
    table.items = payload.list
    table.pages = payload.pages
    table.page = newPage
  } finally {
    table.loading = false
  }
}

function goWrite() {
  if (!auth.isLoggined) {
    return router.push('/login')
  }
  router.push('/board/write')
}

onMounted(() => fetchData(table.page))
</script>

<style scoped>
.board-list-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem 1rem;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.page-title {
  font-size: 2rem;
  font-weight: 700;
  color: #2c3e50;
  margin: 0;
}

.write-btn {
  background: linear-gradient(135deg, #3498db, #2980b9);
  color: white;
  border: none;
  padding: 0.8rem 1.5rem;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.write-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(52, 152, 219, 0.3);
}

.loading-container {
  text-align: center;
  padding: 4rem 0;
  color: #666;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.board-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 1.5rem;
  margin-bottom: 3rem;
}

.empty-state {
  text-align: center;
  padding: 4rem 0;
  color: #666;
}

.empty-state i {
  font-size: 4rem;
  color: #ddd;
  margin-bottom: 1rem;
}

.pagination {
  display: flex;
  justify-content: center;
  gap: 0.5rem;
}

.page-btn {
  padding: 0.6rem 1rem;
  border: 1px solid #ddd;
  background: white;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.page-btn:hover {
  background: #f8f9fa;
  border-color: #3498db;
}

.page-btn.active {
  background: #3498db;
  color: white;
  border-color: #3498db;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .board-list-container {
    padding: 1rem 0.5rem;
  }

  .list-header {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }

  .board-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }

  .page-title {
    font-size: 1.5rem;
  }
}

@media (max-width: 480px) {
  .board-grid {
    grid-template-columns: 1fr;
  }
}
</style>
