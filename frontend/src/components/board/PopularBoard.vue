<template>
  <div class="popular-board-modern">
    <div v-if="loading" class="loading-state">
      <div class="loading-spinner"></div>
      <p>게시글을 불러오는 중...</p>
    </div>
    
    <div v-else-if="!current" class="empty-state">
      <div class="empty-icon">📝</div>
      <p>게시글이 없습니다</p>
    </div>

    <div v-else class="content-card">
      <div class="card-header">
        <div class="post-category">{{ getCategoryName(boardType) }}</div>
        <div class="post-navigation">
          <button class="nav-btn prev" @click="prev" :disabled="list.length <= 1">
            <i class="icon">‹</i>
          </button>
          <span class="nav-counter">{{ idx + 1 }} / {{ list.length }}</span>
          <button class="nav-btn next" @click="next" :disabled="list.length <= 1">
            <i class="icon">›</i>
          </button>
        </div>
      </div>

      <div class="card-body">
        <router-link 
          :to="`/${boardType}/${current.bno}`"
          class="post-title"
        >
          {{ current.title }}
        </router-link>
        
        <p class="post-summary">{{ current.content }}</p>
        
        <div class="post-meta">
          <div class="author-info">
            <div class="author-avatar">{{ current.author.charAt(0) }}</div>
            <span class="author-name">{{ current.author }}</span>
          </div>
          <div class="post-stats">
            <span class="stat-item">
              <i class="stat-icon">👁</i>
              {{ current.views || 0 }}
            </span>
            <span class="stat-item">
              <i class="stat-icon">📅</i>
              {{ formattedDate }}
            </span>
          </div>
        </div>
      </div>

      <div class="card-footer">
        <router-link 
          :to="`/${boardType}`"
          class="view-all-btn"
        >
          전체 보기 →
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, computed } from 'vue'
import type { Board } from '@/hooks/boardService'
import { fetchPopularBoards } from '@/hooks/boardService'

const props = defineProps<{
  title: string
  boardType: 'board' | 'notice' | 'qna'
}>()

const list = ref<Board[]>([])
const loading = ref(true)
const idx = ref(0)

const current = computed(() => list.value[idx.value])

const formattedDate = computed(() => {
  if (!current.value) return ''
  const d = new Date(current.value.createdDate)
  return `${d.getFullYear()}.${String(d.getMonth()+1).padStart(2,'0')}.${String(d.getDate()).padStart(2,'0')}`
})

const getCategoryName = (type: string) => {
  switch(type) {
    case 'board': return '자유게시판'
    case 'notice': return '공지사항'
    case 'qna': return 'Q&A'
    default: return ''
  }
}

async function load() {
  loading.value = true
  idx.value = 0
  try {
    list.value = await fetchPopularBoards(props.boardType)
  } finally {
    loading.value = false
  }
}

function next() {
  if (!list.value.length) return
  idx.value = (idx.value + 1) % list.value.length
}

function prev() {
  if (!list.value.length) return
  idx.value = (idx.value - 1 + list.value.length) % list.value.length
}

onMounted(load)
watch(() => props.boardType, load)
</script>

<style scoped>
.popular-board-modern {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.loading-state,
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem;
  color: #6c757d;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.empty-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
  opacity: 0.5;
}

.content-card {
  background: #f8f9fa;
  border-radius: 16px;
  overflow: hidden;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  background: white;
  border-bottom: 1px solid #e9ecef;
}

.post-category {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  padding: 0.4rem 1rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 600;
}

.post-navigation {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.nav-btn {
  width: 32px;
  height: 32px;
  border: 1px solid #dee2e6;
  background: white;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
}

.nav-btn:hover:not(:disabled) {
  background: #667eea;
  color: white;
  border-color: #667eea;
}

.nav-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.nav-counter {
  font-size: 0.8rem;
  color: #6c757d;
  min-width: 50px;
  text-align: center;
}

.card-body {
  flex: 1;
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
}

.post-title {
  font-size: 1.2rem;
  font-weight: 700;
  color: #2c3e50;
  text-decoration: none;
  margin-bottom: 1rem;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.post-title:hover {
  color: #667eea;
}

.post-summary {
  flex: 1;
  color: #6c757d;
  line-height: 1.6;
  margin-bottom: 1.5rem;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.post-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.author-avatar {
  width: 32px;
  height: 32px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 0.9rem;
}

.author-name {
  font-weight: 500;
  color: #495057;
}

.post-stats {
  display: flex;
  gap: 1rem;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  color: #6c757d;
  font-size: 0.8rem;
}

.stat-icon {
  font-size: 0.9rem;
}

.card-footer {
  padding: 1rem 1.5rem;
  background: white;
  border-top: 1px solid #e9ecef;
}

.view-all-btn {
  display: inline-block;
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.2s ease;
}

.view-all-btn:hover {
  color: #764ba2;
}

@media (max-width: 768px) {
  .post-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
  
  .post-stats {
    gap: 0.5rem;
  }
  
  .card-header {
    padding: 1rem;
  }
  
  .card-body {
    padding: 1rem;
  }
}
</style>
