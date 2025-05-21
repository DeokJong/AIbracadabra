<template>
  <v-data-table
    :loading="table.loading"
    :headers="table.headers"
    :items="table.items"
    v-model:items-per-page="table.itemsPerPage"
    items-per-page-text="페이지당 데이터 수"
    no-data-text="데이터가 없습니다."
    class="elevation-1"

    hide-default-footer
  />
</template>

<script setup lang="ts">
export type BoardSummary = {
  id: number
  title: string
  author: string
  createdAt: string
  views: number
}

import axios from 'axios'
import { reactive, onMounted, watch } from 'vue'
import { useToast } from 'vue-toastification'


const props = defineProps({
  apiCallUrl: {
    type: String,
    required: true
  }
})

const table = reactive({
  loading: true,
  headers: [
    { title: '글번호',  value: 'id' },
    { title: '제목',    value: 'title' },
    { title: '작성자',  value: 'author' },
    { title: '작성일',  value: 'createdAt' },
    { title: '조회수',  value: 'views' },
  ],
  items: [] as BoardSummary[],
  itemsPerPage: 20
})

onMounted(async () => {
  console.log('apiCallUrl', props.apiCallUrl)
  table.items = await axios.get<BoardSummary[]>(props.apiCallUrl)
    .then((response) => {
      return response.data
    })
    .catch(() => {
      useToast().info('데이터를 가져오는 중 오류가 발생했습니다.')
      return []
    })
})

watch(table.items, () => {
  table.loading = false
}, { immediate: true })


</script>
