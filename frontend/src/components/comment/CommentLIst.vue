<template>
  <div class="comments-wrap">
    <div class="comments-header">댓글 ({{ comments.length }})</div>
    <CommentItem
      v-for="c in comments"
      :key="c.cno"
      :comment="c"
      :current-user="currentUser"
      @edit="$emit('edit', $event)"
      @delete="$emit('delete', $event)"
    />
  </div>
</template>

<script setup lang="ts">
import { defineProps, defineEmits } from 'vue'
import CommentItem from './CommentItem.vue'

type Comment = {
  cno: number;
  bno: number;
  author: string;
  content: string;
  createdDate: string;
  updatedDate: string;
}

const props = defineProps<{
  comments: Comment[]
  currentUser: string
}>()

const emit = defineEmits<{
  (e: 'edit', comment: Comment): void
  (e: 'delete', comment: Comment): void
}>()
</script>

<style scoped>
.comments-header { font-size: 1.125rem; font-weight: 500; margin-bottom: 12px; }
</style>
