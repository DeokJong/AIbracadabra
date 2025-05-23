<template>
  <ItemTable
    :items="table.items"
    :pages="table.pages"
    :currentPage="table.page"
    :loading="table.loading"
    :total-items="table.totalItems" 
    @update:currentPage="fetchData"
    @write="goWrite"
  />
</template>

<script setup lang="ts">
import { reactive, onMounted } from 'vue'
import axios from 'axios'
import { useRouter, useRoute } from 'vue-router'
import { useAuth } from '@/hooks/useAuth'
import ItemTable, { BoardSummary } from '@/components/qna/ItemTable.vue'

const auth   = useAuth()
const router = useRouter()
const route  = useRoute()

const API_URL = '/api/v1/board'

const table = reactive({
  items: [] as BoardSummary[],
  page:  Number(route.query.currentPage) || 1,
  pages: 0,
  loading: false,
})

async function fetchData(newPage = table.page) {
  table.loading = true
  try {
    const res = await axios.get(API_URL, {
      params: {
        boardType:   'qna',         // 반드시 boardType 파라미터 첨부
        currentPage: newPage,
        pageSize:    table.itemsPerPage ?? 20
      }
    })
    // 컨트롤러 Impl 에서 직접 PageInfo 반환하거나, handleResponse wrapper 사용 여부에 따라
    // res.data.data || res.data 중 하나를 택하도록 처리
    const payload = res.data.data ?? res.data
    table.items = payload.list
    table.pages = payload.pages
    table.page  = newPage
  } finally {
    table.loading = false
  }
}

function goWrite() {
  if (!auth.isLoggined) {
    return router.push('/login')
  }
  router.push('/qna/write')
}

onMounted(() => fetchData(table.page))
</script>
