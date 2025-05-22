<template>
  <div class="region-news">
    <h2 class="region-title">{{ regionName }}</h2>

    <div v-if="loading" class="loading">로딩 중...</div>
    <div v-else-if="!currentItem" class="no-data">해당 지역의 뉴스가 없습니다.</div>

    <div v-else class="card">
      <!-- 본문 -->
      <a
        class="title"
        :href="currentItem.url"
        target="_blank"
        rel="noopener noreferrer"
      >
        {{ currentItem.title }}
      </a>
      <p class="summary">{{ currentItem.summary }}</p>

      <!-- 발행일 -->
      <div class="footer">
        <span class="date">{{ formattedDate }}</span>
      </div>

      <!-- 이전/다음 버튼 -->
      <div class="controls">
        <button @click="prev" class="nav-btn">◀ 이전</button>
        <button @click="next" class="nav-btn">다음 ▶</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, computed } from 'vue'
import type { News } from '@/hooks/newsService'
import { fetchNewsBySidoCode } from '@/hooks/newsService'

const props = defineProps<{
  regionName: string
  sidoCode: number
}>()

const newsList = ref<News[]>([])
const loading = ref(true)
const idx = ref(0)
const currentItem = computed(() => newsList.value[idx.value])
const formattedDate = computed(() => {
  if (!currentItem.value) return ''
  const d = new Date(currentItem.value.publishAt)
  return `${d.getFullYear()}.${String(d.getMonth()+1).padStart(2,'0')}.${String(d.getDate()).padStart(2,'0')}`
})

function next() {
  if (!newsList.value.length) return
  idx.value = (idx.value + 1) % newsList.value.length
}
function prev() {
  if (!newsList.value.length) return
  idx.value = (idx.value - 1 + newsList.value.length) % newsList.value.length
}

async function load() {
  loading.value = true
  idx.value = 0
  try {
    newsList.value = await fetchNewsBySidoCode(props.sidoCode)
  } finally {
    loading.value = false
  }
}

onMounted(load)
watch(() => props.sidoCode, load)
</script>

<style scoped>
.region-news {
  display: flex;
  flex-direction: column;
}
.region-title {
  font-size: 1.1rem;
  text-align: center;
  margin-bottom: 0.5rem;
}

/* 카드 스타일 */
.card {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  border: 1px solid #ddd;       /* 카드 테두리 */
  border-radius: 8px;
  padding: 1rem;
  background: #fff;
  min-height: 180px;             /* 충분한 높이로 버튼 보장 */
  transition: box-shadow 0.2s;
}
.card:hover {
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

/* 제목/요약/날짜 */
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
.footer {
  display: flex;
  justify-content: flex-end;
}
.date {
  font-size: 0.8rem;
  color: #999;
}

/* 이전/다음 컨트롤 */
.controls {
  display: flex;
  justify-content: space-between;
  margin-top: auto;              /* 카드 하단으로 고정 */
}
.nav-btn {
  padding: 0.4rem 0.8rem;
  background: transparent;       /* 투명 배경 */
  color: #3498db;                /* 글자색 */
  border: 1px solid #3498db;     /* 테두리 버튼 */
  border-radius: 4px;
  font-size: 0.85rem;
  cursor: pointer;
  transition: background 0.2s, color 0.2s;
}
.nav-btn:hover {
  background: #3498db;
  color: #fff;
}

/* 로딩/데이터 없음 */
.loading, .no-data {
  text-align: center;
  color: #666;
  padding: 1rem 0;
}
.region-news .card {
  /* 1) 고정 높이 부여 */
  height: 260px;              /* 필요에 따라 조절하세요 */
  display: flex;
  flex-direction: column;
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: #fff;

  /* 2) hover 효과 */
  transition: box-shadow 0.2s;
}
.region-news .card:hover {
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

/* 제목 */
.region-news .title {
  font-size: 1rem;
  font-weight: bold;
  margin-bottom: 0.5rem;
  color: #333;
  text-decoration: none;
}
.region-news .title:hover {
  text-decoration: underline;
}

/* 요약: 3줄로 자르고 넘치는 부분 숨김 */
.region-news .summary {
  flex: 1;                   /* 남은 공간 전부 차지 */
  font-size: 0.9rem;
  color: #555;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-bottom: 0.75rem;
}

/* 발행일 */
.region-news .footer {
  font-size: 0.8rem;
  color: #999;
  text-align: right;
}

/* 이전/다음 버튼 */
.region-news .controls {
  display: flex;
  justify-content: space-between;
  margin-top: auto;          /* 항상 카드 맨 아래로 */
}
.region-news .nav-btn {
  padding: 0.4rem 0.8rem;
  background: transparent;
  color: #3498db;
  border: 1px solid #3498db;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.2s, color 0.2s;
}
.region-news .nav-btn:hover {
  background: #3498db;
  color: #fff;
}
</style>
