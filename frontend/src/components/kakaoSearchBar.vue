
<template>
  <form @submit.prevent="onSearch" style="text-align:center; margin-bottom:16px;">
    <input
      v-model="keyword"
      placeholder="검색어를 입력하세요"
      style="width:200px; padding:4px;"
      autocomplete="off"
    />
    <button type="submit" style="padding:4px 8px;">검색</button>
  </form>
</template>

<script setup>
import { ref } from 'vue'
const emits = defineEmits(['search'])
const keyword = ref('')

function onSearch() {
  emits('search', keyword.value.trim())
}
</script>
