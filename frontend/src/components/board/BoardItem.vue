<template>
  <div class="board-item">
    <a
      class="title"
      :href="`/board/${item.bno}`"
    >
      {{ item.title }}
    </a>
    <p class="summary">{{ item.content }}</p>
    <div class="info">
      <span class="author">{{ item.author }}</span>
      <span class="date">{{ formattedDate }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps, computed } from 'vue'
import type { Board } from '@/hooks/boardService'

const props = defineProps<{ item: Board }>()

const formattedDate = computed(() => {
  const d = new Date(props.item.createdDate)
  return `${d.getFullYear()}.${String(d.getMonth()+1).padStart(2,'0')}.${String(d.getDate()).padStart(2,'0')}`
})
</script>

<style scoped>
.board-item {
  border-bottom: 1px solid #eee;
  padding: 0.75rem 0;
}
.title {
  font-weight: bold;
  color: #3498db;
  text-decoration: none;
}
.title:hover {
  text-decoration: underline;
}
.summary {
  margin: 0.4rem 0;
  color: #555;
  font-size: 0.9rem;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.info {
  display: flex;
  justify-content: space-between;
  font-size: 0.8rem;
  color: #999;
}
</style>
