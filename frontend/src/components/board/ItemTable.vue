<template>
  <v-data-table
    :loading="table.loading"
    :headers="table.headers"
    :items="table.items"
    v-model:items-per-page="table.itemsPerPage"
    v-model:page="table.page"
    :server-items-length="table.totalItems"
    items-per-page-text="페이지당 데이터 수"
    no-data-text="데이터가 없습니다."
    class="elevation-1"

    hide-default-footer
  >
  <template #item="{ item, props }">
      <tr v-bind="props" @click="() => onRowClick(item)" class="row">
        <td>{{ item.bno }}</td>
        <td>{{ item.title }}</td>
        <td>{{ item.author }}</td>
        <td>{{ item.createdDate }}</td>
        <td>{{ item.views }}</td>
      </tr>
    </template>
  </v-data-table>
</template>

<script setup lang="ts">
export type BoardSummary = {
  bno: number
  title: string
  author: string
  createdDate: string
  views: number
}

import { CommonResponse } from '@/service/common'
import axios from 'axios'

import { reactive, onMounted } from 'vue'
import { useToast } from 'vue-toastification'
import { useRouter } from 'vue-router'
import { useAuth } from '@/hooks/useAuth'

const auth = useAuth()

const router = useRouter();

const props = defineProps({
  apiCallUrl: {
    type: String,
    required: true
  }
})

const table = reactive({
  loading: true,
  headers: [
    { title: '글번호',  value: 'bno' },
    { title: '제목',    value: 'title' },
    { title: '작성자',  value: 'author' },
    { title: '작성일',  value: 'createdDate' },
    { title: '조회수',  value: 'views' },
    { title: ' ',       value: 'actions', sortable: false },

  ],
  items: [] as BoardSummary[],
  page: 1,
  itemsPerPage: 20,  //
  totalItems: 0,  // 이거는 서버에서 보내주는 거임 그래서  서버단에서 meta로 데이터 묶어서 하기

})

onMounted(async () => {
  table.items = await axios.get<CommonResponse<BoardSummary[]>>(props.apiCallUrl)
    .then((response) => {
      return response.data.data
    })
    .catch(() => {
      useToast().info('데이터를 가져오는 중 오류가 발생했습니다.')
      return []
    })
  table.loading = false
})

const onRowClick = (item: BoardSummary) => {
  console.log(item)         // { id: ..., title: ..., … }
  console.log(item.bno)      // 숫자 ID
  // router 이용해서 자세한 페이지로 넘기기
  console.log(JSON.stringify(item))
  router.push({
    path: `/board/${item.bno}`
    })
}

</script>

<style scoped>
.row:hover {
  background-color: lightgray;
  cursor: pointer;
}
</style>
