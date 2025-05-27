<template>
  <div class="modern-news-card">
    <!-- 카드 헤더 -->
    <div class="card-header">
      <div class="news-badge">
        <span class="badge-icon">📰</span>
        NEWS
      </div>
      <router-link to="/news" class="view-all-btn" title="전체 뉴스 보기">
        <span class="btn-text">전체보기</span>
        <span class="btn-icon">→</span>
      </router-link>
    </div>

    <!-- 카드 본문 -->
    <div class="card-body">
      <a
        class="news-title"
        :href="item.url"
        target="_blank"
        rel="noopener noreferrer"
      >
        {{ item.title }}
      </a>
      <p class="news-summary">{{ item.summary }}</p>
    </div>

    <!-- 카드 푸터 -->
    <div class="card-footer">
      <div class="publish-info">
        <span class="date-icon">📅</span>
        <span class="publish-date">{{ formattedDate }}</span>
      </div>
      <div class="external-indicator">
        <span class="external-icon">↗</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps, computed } from 'vue'
import type { News } from '@/hooks/newsService'

const props = defineProps<{ item: News }>()

const formattedDate = computed(() => {
  return new Date(props.item.publishAt)
    .toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    })
})
</script>

<style scoped>
.modern-news-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: all 0.3s ease;
  position: relative;
  border: 1px solid #f0f0f0;
}

.modern-news-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
}

.card-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 1rem 1.5rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.news-badge {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: white;
  font-weight: 600;
  font-size: 0.9rem;
}

.badge-icon {
  font-size: 1.1rem;
}

.view-all-btn {
  display: flex;
  align-items: center;
  gap: 0.3rem;
  color: white;
  text-decoration: none;
  background: rgba(255, 255, 255, 0.1);
  padding: 0.4rem 0.8rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 500;
  transition: all 0.2s ease;
}

.view-all-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateX(2px);
}

.card-body {
  padding: 1.5rem;
  flex: 1;
}

.news-title {
  display: block;
  font-size: 1.1rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 1rem;
  text-decoration: none;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.news-title:hover {
  color: #667eea;
}

.news-summary {
  font-size: 0.9rem;
  color: #6c757d;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin: 0;
}

.card-footer {
  padding: 1rem 1.5rem;
  border-top: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fafafa;
}

.publish-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.date-icon {
  font-size: 0.9rem;
}

.publish-date {
  font-size: 0.8rem;
  color: #999;
}

.external-indicator {
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
  .card-header {
    padding: 1rem;
  }
  
  .card-body {
    padding: 1rem;
  }
  
  .card-footer {
    padding: 1rem;
  }
}
</style>
