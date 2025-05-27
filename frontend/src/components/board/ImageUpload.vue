<!-- src/components/ImageUploader.vue -->
<template>
  <div>
    <input type="file" accept="image/*" @change="onChange" />
    <img v-if="preview" :src="preview" class="preview" />
  </div>
</template>

<script setup>
import { ref, defineEmits } from 'vue';

const preview = ref('');
const emits = defineEmits(['file-selected']);

function onChange(e) {
  const file = e.target.files[0];
  if (!file?.type.startsWith('image/')) {
    return alert('이미지 파일만 선택해주세요');
  }
  preview.value = URL.createObjectURL(file);
  emits('file-selected', file);
}
</script>

<style>
.preview { max-width: 200px; margin-top: 8px; }
</style>
