<template>
  <div class="news-item">
    <a :href="item.url" target="_blank" rel="noopener" class="title">
      {{ item.title }}
    </a>
    <p class="summary">{{ item.summary || '요약 정보 없음' }}</p>
    <small class="date">{{ formattedDate }}</small>
  </div>
</template>

<script setup lang="ts">
import { defineProps, computed } from 'vue'
import type { News } from '@/services/newsService'

const props = defineProps<{ item: News }>()

const formattedDate = computed(() => {
  return new Date(props.item.publishAt)
    .toLocaleString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    })
})
</script>

<style scoped>
.news-item {
  padding: 0.5rem 0;
  border-bottom: 1px solid #eee;
}
.news-item .title {
  font-size: 1.1rem;
  font-weight: bold;
  color: #333;
  text-decoration: none;
}
.news-item .summary {
  margin: 0.3rem 0;
  color: #555;
}
.news-item .date {
  color: #999;
  font-size: 0.8rem;
}
</style>
