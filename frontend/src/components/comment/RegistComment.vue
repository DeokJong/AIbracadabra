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
      <v-btn
        color="primary"
        text
        @click="onSubmit"
        :disabled="!content.trim()"
      >
        등록
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { defineProps, defineEmits } from 'vue'

const props = defineProps<{ bno: number }>()

// 반드시 submitted / cancelled 로 정의
const emit = defineEmits<{
  (e: 'submitted', content: string): void
  (e: 'cancelled'): void
}>()

const content = ref('')

function onCancel() {
  emit('cancelled')
}

function onSubmit() {
  // 1) 작성된 텍스트만 부모로 전달
  emit('submitted', content.value.trim())
  content.value = ''
}
</script>

<style scoped>
.comment-form {
  border-radius: 8px;
  background: #fafafa;
}
</style>
