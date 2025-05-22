<template>
  <v-data-table
    :loading="table.loading"
    :headers="table.headers"
    :items="noticeItems"
    v-model:items-per-page="table.itemsPerPage"
    v-model:page="table.page"
    :server-items-length="noticeItems.length"
    items-per-page-text="페이지당 데이터 수"
    no-data-text="데이터가 없습니다."
    class="elevation-1"
    hide-default-footer
  >
    <template #item="{ item, props }">
      <tr
        v-bind="props"
        @click="() => onRowClick(item)"
        class="row"
      >
        <td>{{ item.bno }}</td>
        <td>{{ item.title }}</td>
        <td>{{ item.author }}</td>
        <td>{{ item.createdDate }}</td>
        <td>{{ item.views/2 }}</td>
      </tr>
    </template>
  </v-data-table>

  <div class="button-wrap" v-if="auth.userInfo?.role === 'admin'">
    <v-btn color="primary" @click="handleWriteClick">
      글쓰기
    </v-btn>
  </div>
</template>

<script setup lang="ts">
import { reactive, onMounted, computed } from 'vue'
import axios from 'axios'
import { useToast } from 'vue-toastification'
import { useRouter } from 'vue-router'
import { useAuth } from '@/hooks/useAuth'

/** 테이블에 들어갈 아이템 타입 정의 */
export type BoardSummary = {
  bno: number
  title: string
  author: string
  createdDate: string
  views: number
  boardType: string
}

// hooks & utils
const auth = useAuth()
const router = useRouter()
const toast = useToast()

// 부모로부터 API URL을 받아옴
const props = defineProps<{
  apiCallUrl: string
}>()

// 테이블 상태 및 데이터
const table = reactive({
  loading: true,
  headers: [
    { title: '글번호', value: 'bno' },
    { title: '제목',   value: 'title' },
    { title: '작성자', value: 'author' },
    { title: '작성일', value: 'createdDate' },
    { title: '조회수', value: 'views' },
    { title: ' ',      value: 'actions', sortable: false },
  ] as { title: string; value: string; sortable?: boolean }[],
  items: [] as BoardSummary[],
  page: 1,
  itemsPerPage: 20,
  totalItems: 0,
})

// **공지사항(‘notice’)만** 뽑아서 새 배열 생성
const noticeItems = computed(() =>
  table.items.filter(i => i.boardType === 'notice')
)

onMounted(async () => {
  try {
    const res = await axios.get(props.apiCallUrl)
    const data = res.data.data // { total, list, ... }
    table.items      = Array.isArray(data.list) ? data.list : []
    table.totalItems = data.total ?? table.items.length
  } catch {
    toast.info('데이터를 가져오는 중 오류가 발생했습니다.')
    table.items      = []
    table.totalItems = 0
  } finally {
    table.loading = false
  }
})

// 행 클릭 -> 상세 페이지
const onRowClick = (item: BoardSummary) => {
  router.push(`/notice/${item.bno}`)
}

// 글쓰기 클릭 -> 로그인 체크 후 이동
const handleWriteClick = () => {
  if (!auth.isLoggined) {
    toast.info('로그인이 필요합니다.')
    router.push('/login')
  } else {
    router.push('/notice/write')
  }
}
</script>

<style scoped>
.row:hover {
  background-color: lightgray;
  cursor: pointer;
}
.button-wrap {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
