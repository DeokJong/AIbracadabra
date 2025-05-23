<!-- src/components/news/NewsCard.vue -->
<template>
  <div class="news-card">
    <!-- 상단 우측 전체 뉴스 보기 버튼 -->
    <router-link to="/news" class="all-btn" title="전체 뉴스 보기">＋</router-link>

    <!-- 본문: 제목 + 요약 -->
    <div class="body">
      <a
        class="title"
        :href="item.url"
        target="_blank"
        rel="noopener noreferrer"
      >
        {{ item.title }}
      </a>
      <p class="summary">{{ item.summary }}</p>
    </div>

    <!-- 푸터: 왼쪽 기자 이름, 오른쪽 발행일 -->
    <div class="footer">
      <span class="date">{{ formattedDate }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps, computed } from 'vue'
import type { News } from '@/hooks/newsService'

const props = defineProps<{ item: News }>()

// 날짜 포맷팅
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
.news-card {
  display: flex;
  flex-direction: column;
  position: relative;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  overflow: hidden;
}

/* 전체 보기 버튼 */
.all-btn {
  position: absolute;
  top: 8px;
  right: 8px;
  width: 28px;
  height: 28px;
  line-height: 28px;
  text-align: center;
  background: rgba(0,0,0,0.5);
  color: #fff;
  border-radius: 50%;
  text-decoration: none;
}
.all-btn:hover {
  background: rgba(0,0,0,0.7);
}

/* 본문 */
.body {
  flex: 1;
  padding: 1rem;
}
.title {
  display: block;
  font-size: 1.1rem;
  font-weight: 600;
  color: #222;
  margin-bottom: 0.5rem;
  text-decoration: none;
}
.title:hover {
  text-decoration: underline;
}
.summary {
  font-size: 0.9rem;
  color: #555;
  line-height: 1.4;
  max-height: 4.2em;   /* 약 3줄까지 보이도록 */
  overflow: hidden;
}

/* 푸터 */
.footer {
  display: flex;
  justify-content: space-between;
  padding: 0.8rem 1rem;
  border-top: 1px solid #f0f0f0;
  font-size: 0.85rem;
  color: #777;
}
.reporter {
  /* 왼쪽 정렬은 기본 */
}
.date {
  /* 오른쪽 정렬은 justify-content 덕분에 자동 */
}
</style>
