<template>
  <ItemTable
    :items="items"
    :pages="pages"
    :currentPage="currentPage"
    :loading="loading"
    @update:currentPage="page => currentPage = page"
    @write="onWrite"
  />
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import ItemTable, { BoardSummary } from '@/components/board/ItemTable.vue'
import { useMyBoards } from '@/hooks/useMyBoards'

const router = useRouter()

// 훅에서 정의한 reactive 상태 가져오기
const { items, pages, currentPage, loading } = useMyBoards()

// 글쓰기 버튼 클릭 시 이동
function onWrite() {
  router.push('/board/write')
}
</script>
