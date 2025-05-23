<template>
  <v-card class="pa-4 my-4 comment-form">
    <v-textarea
      v-model="content"
      label="댓글을 입력하세요"
      rows="3"
      auto-grow
    />
    <v-card-actions class="justify-end">
      <v-btn text @click="onCancel">취소</v-btn>
      <v-btn color="primary" text @click="onSubmit" :disabled="!content.trim()">
        등록
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'
import { useToast } from 'vue-toastification'
import { defineProps, defineEmits } from 'vue'

type Comment = {
  cno: number;
  bno: number;
  author: string;
  content: string;
  createdDate: string;
  updatedDate: string | null;
}

const props = defineProps<{
  bno: number
}>()

const emit = defineEmits<{
  (e: 'submitted', comment: Comment): void
  (e: 'cancelled'): void
}>()

const toast = useToast()
const content = ref('')

const onCancel = () => emit('cancelled')

const onSubmit = async () => {
  try {
    const res = await axios.post(`/api/v1/board/${props.bno}/comment`, { content: content.value })
    const newComment = res.data.data as Comment
    toast.success('댓글이 등록되었습니다.')
    emit('submitted', newComment)
    content.value = ''
  } catch {
    toast.error('댓글 등록에 실패했습니다.')
  }
}
</script>

<style scoped>
.comment-form { border-radius: 8px; background: #fafafa; }
</style>
