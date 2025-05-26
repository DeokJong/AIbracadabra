<template>
  <div class="modern-region-news">
    <div class="region-header">
      <h3 class="region-title">
        <span class="location-icon">📍</span>
        {{ regionName }}
      </h3>
      <div class="news-controls" v-if="newsList.length > 1">
        <button @click="prev" class="control-btn prev" :disabled="newsList.length <= 1">
          <span>‹</span>
        </button>
        <span class="news-counter">{{ idx + 1 }}/{{ newsList.length }}</span>
        <button @click="next" class="control-btn next" :disabled="newsList.length <= 1">
          <span>›</span>
        </button>
      </div>
    </div>

    <div class="news-content">
      <div v-if="loading" class="loading-state">
        <div class="loading-spinner"></div>
        <p>뉴스를 불러오는 중...</p>
      </div>
      
      <div v-else-if="!currentItem" class="empty-state">
        <div class="empty-icon">📰</div>
        <p>해당 지역의 뉴스가 없습니다</p>
      </div>

      <div v-else class="news-item">
        <a
          class="item-title"
          :href="currentItem.url"
          target="_blank"
          rel="noopener noreferrer"
        >
          {{ currentItem.title }}
        </a>
        
        <p class="item-summary">{{ currentItem.summary }}</p>
        
        <div class="item-footer">
          <div class="publish-info">
            <span class="calendar-icon">📅</span>
            <span class="publish-date">{{ formattedDate }}</span>
          </div>
          <div class="external-link">
            <span class="link-icon">↗</span>
          </div>
        </div>
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
.modern-region-news {
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: all 0.3s ease;
  height: 320px;
  display: flex;
  flex-direction: column;
}

.modern-region-news:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

.region-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 1rem 1.5rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: white;
}

.region-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1.1rem;
  font-weight: 600;
  margin: 0;
}

.location-icon {
  font-size: 1.2rem;
}

.news-controls {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.control-btn {
  width: 28px;
  height: 28px;
  background: rgba(255, 255, 255, 0.2);
  border: none;
  border-radius: 50%;
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
  font-size: 1.2rem;
}

.control-btn:hover:not(:disabled) {
  background: rgba(255, 255, 255, 0.3);
  transform: scale(1.1);
}

.control-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.news-counter {
  font-size: 0.8rem;
  min-width: 35px;
  text-align: center;
}

.news-content {
  flex: 1;
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
}

.loading-state,
.empty-state {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #6c757d;
}

.loading-spinner {
  width: 32px;
  height: 32px;
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
  font-size: 2.5rem;
  margin-bottom: 0.5rem;
  opacity: 0.5;
}

.news-item {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.item-title {
  font-size: 1rem;
  font-weight: 600;
  color: #2c3e50;
  text-decoration: none;
  margin-bottom: 1rem;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.item-title:hover {
  color: #667eea;
}

.item-summary {
  flex: 1;
  font-size: 0.9rem;
  color: #6c757d;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-bottom: 1rem;
}

.item-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
}

.publish-info {
  display: flex;
  align-items: center;
  gap: 0.3rem;
}

.calendar-icon {
  font-size: 0.9rem;
}

.publish-date {
  font-size: 0.8rem;
  color: #999;
}

.external-link {
  background: #667eea;
  color: white;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.8rem;
}

@media (max-width: 768px) {
  .modern-region-news {
    height: auto;
    min-height: 280px;
  }
  
  .region-header {
    padding: 1rem;
  }
  
  .news-content {
    padding: 1rem;
  }
}
</style>
