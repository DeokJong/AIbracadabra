// CommentItem.vue
<template>
  <div class="comment-bubble mb-3">
    <v-avatar size="32" class="mr-2">
      <span>{{ comment.author.charAt(0) }}</span>
    </v-avatar>
    <div class="bubble-body">
      <div class="bubble-meta">{{ comment.author }} · {{ comment.createdDate }}</div>
      <div class="bubble-content">{{ comment.content }}</div>
      <div class="comment-actions mt-2" v-if="comment.author === currentUser">
        <v-btn small text @click="$emit('edit', comment)">수정</v-btn>
        <v-btn small text color="error" @click="$emit('delete', comment)">삭제</v-btn>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps, defineEmits } from 'vue'

type Comment = {
  cno: number;
  bno: number;
  author: string;
  content: string;
  createdDate: string;
  updatedDate: string;
}

const props = defineProps<{
  comment: Comment
  currentUser: string
}>()

const emit = defineEmits<{
  (e: 'edit', comment: Comment): void
  (e: 'delete', comment: Comment): void
}>()
</script>

<style scoped>
.comment-bubble { display: flex; align-items: flex-start; }
.bubble-body { background: #f5f5f5; border-radius: 16px; padding: 12px 16px; flex: 1; }
.bubble-meta { font-size: 0.75rem; color: #999; margin-bottom: 4px; }
.bubble-content { font-size: 0.95rem; color: #444; white-space: pre-wrap; }
.comment-actions { display: flex; gap: 8px; }
</style>
