<template>
  <div class="category-board">
    <!-- 섹션 제목(왼쪽 정렬) -->
    <h2 class="category-title">{{ title }}</h2>

    <!-- 로딩/데이터 없음 -->
    <div v-if="loading" class="loading">로딩 중...</div>
    <div v-else-if="!current" class="no-data">글이 없습니다.</div>

    <!-- 카드 -->
    <div v-else class="card">
      <!-- 글 제목 -->
      <a
        class="title"
        :href="`/board/${current.bno}`"
        target="_blank"
        rel="noopener noreferrer"
      >
        {{ current.title }}
      </a>

      <!-- 요약 -->
      <p class="summary">{{ current.content }}</p>

      <!-- 작성자 & 날짜 -->
      <div class="info">
        <span class="author">{{ current.author }}</span>
        <span class="date">{{ formattedDate }}</span>
      </div>

      <!-- 이전/다음 버튼 -->
      <div class="controls">
        <button class="nav-btn" @click="prev">◀ 이전</button>
        <button class="nav-btn" @click="next">다음 ▶</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, computed } from 'vue'
import type { Board } from '../../hooks/boardService'
import { fetchPopularBoards } from '../../hooks/boardService'

const props = defineProps<{
  title: string
  boardType: 'board' | 'notice' | 'qna'
}>()

const list = ref<Board[]>([])
const loading = ref(true)
const idx = ref(0)

// 현재 보여줄 글
const current = computed(() => list.value[idx.value])

// YYYY.MM.DD 포맷
const formattedDate = computed(() => {
  if (!current.value) return ''
  const d = new Date(current.value.createdDate)
  return `${d.getFullYear()}.${String(d.getMonth()+1).padStart(2,'0')}.${String(d.getDate()).padStart(2,'0')}`
})

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
.category-board {
  display: flex;
  flex-direction: column;
}
.category-title {
  font-size: 1.15rem;
  font-weight: 600;
  text-align: left;       /* 왼쪽 정렬 */
  margin-bottom: 0.5rem;
  padding-left: 0.5rem;
}

/* 카드 스타일 */
.card {
  display: flex;
  flex-direction: column;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 1rem;
  background: #fff;
  min-height: 260px;
  transition: box-shadow 0.2s;
}
.card:hover {
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

/* 제목 */
.title {
  font-size: 1rem;
  font-weight: bold;
  color: #333;
  text-decoration: none;
  margin-bottom: 0.5rem;
}
.title:hover {
  text-decoration: underline;
}

/* 요약 */
.summary {
  flex: 1;
  font-size: 0.9rem;
  color: #555;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-bottom: 0.75rem;
}

/* 작성자 + 날짜 */
.info {
  display: flex;
  justify-content: space-between;
  font-size: 0.8rem;
  color: #999;
  margin-bottom: 0.75rem;
}

/* 이전/다음 버튼 */
.controls {
  display: flex;
  justify-content: space-between;
  margin-top: auto;
}
.nav-btn {
  padding: 0.4rem 0.8rem;
  background: transparent;
  color: #3498db;
  border: 1px solid #3498db;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.2s, color 0.2s;
}
.nav-btn:hover {
  background: #3498db;
  color: #fff;
}

/* 로딩/데이터 없음 */
.loading,
.no-data {
  text-align: center;
  color: #666;
  padding: 1rem 0;
  font-style: italic;
}
</style>
