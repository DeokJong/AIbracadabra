<template>
  <v-dialog
    v-model="show"
    max-width="500"
    persistent
    scrollable
    @click:outside="onCancel"
  >
    <v-card>
      <v-card-title class="text-h5">댓글 삭제</v-card-title>
      <v-card-text>정말 삭제하시겠습니까?</v-card-text>
      <v-card-actions class="justify-end">
        <v-btn text @click="onCancel">취소</v-btn>
        <v-btn class="bg-red" text @click="onConfirm">예</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { defineEmits } from 'vue'

const emit = defineEmits<{
  (e: 'resolve'): void  // 확인
  (e: 'close'): void    // 취소
}>()

const show = ref(true)
function onConfirm() {
  show.value = false
  setTimeout(() => emit('resolve'), 300)
}
function onCancel() {
  show.value = false
  setTimeout(() => emit('close'), 300)
}
</script>
