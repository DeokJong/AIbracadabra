<template>
  <v-data-table
    :loading="loading"
    :headers="headers"
    :items="items"
    v-model:items-per-page="itemsPerPage"
    hide-default-footer
    class="elevation-1"
  >
    <template #item="{ item, props }">
      <tr v-bind="props" @click="(item.visibility === 'PUBLIC' || item.mno===userInfo.mno) &&  onRowClick(item)" class="row">
        <td>
          {{ item.visibility === 'PRIVATE'
            ? '비공개 글입니다.'
            : item.title
          }}
        </td>        
        <td>{{ item.author}}</td>
        <td>{{ item.createdDate }}</td>
        <td>{{ item.views }}</td>
      </tr>
    </template>
  </v-data-table>

  <v-pagination
    v-model="currentPageInternal"
    :length="pages"
    @update:model-value="onPageChange"
    class="mt-4"
  />

  <div class="button-wrap">
    <v-btn color="primary" @click="$emit('write')">글쓰기</v-btn>
  </div>
</template>

<script setup lang="ts">
import { useAuth } from '@/hooks/useAuth'
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'

const userInfo = useAuth().userInfo


export interface BoardSummary {
  bno: number
  mno: number
  title: string
  author: string
  createdDate: string
  views: number
  boardType: string
  visibility: 'PUBLIC' | 'PRIVATE'

}

// 부모로부터 받는 props
const props = defineProps<{
  items: BoardSummary[]
  pages: number
  currentPage: number
  loading: boolean
}>()

// 자식이 emit 할 이벤트
const emit = defineEmits<{
  (e: 'update:currentPage', v: number): void
  (e: 'write'): void
}>()

const router = useRouter()
const itemsPerPage = 20
const headers = [
  { title: '제목',   value: 'title' },
  { title: '작성자', value: 'author' },
  { title: '작성일', value: 'createdDate' },
  { title: '조회수', value: 'views' },
]

// v-pagination 과 부모 currentPage 동기화
const currentPageInternal = ref(props.currentPage)
watch(() => props.currentPage, v => (currentPageInternal.value = v))

// 페이지 변경 시 부모에게 알림
function onPageChange(v: number) {
  emit('update:currentPage', v)
}

// row 클릭 시 상세 페이지로 이동
function onRowClick(item: BoardSummary) {
  router.push(`/board/${item.bno}`)
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
