<template>
  <div class="board-table-container">
    <v-data-table
      :loading="loading"
      :headers="headers"
      :items="items"
      v-model:items-per-page="itemsPerPage"
      hide-default-footer
      class="elegant-table elevation-3"
    >
      <template #item="{ item, props }">
        <tr v-bind="props" @click="(item.visibility === 'PUBLIC' || item.mno===userInfo.mno) &&  onRowClick(item)" class="table-row">
          <td class="title-cell">
            <div class="title-content">
              <span v-if="item.visibility === 'PRIVATE'" class="private-badge">🔒</span>
              {{ item.visibility === 'PRIVATE'
                ? '비공개 글입니다.'
                : item.title
              }}
            </div>
          </td>        
          <td class="author-cell">{{ item.author}}</td>
          <td class="date-cell">{{ item.createdDate }}</td>
          <td class="views-cell">
            <span class="views-badge">{{ item.views }}</span>
          </td>
        </tr>
      </template>
    </v-data-table>

    <v-pagination
      v-model="currentPageInternal"
      :length="pages"
      @update:model-value="onPageChange"
      class="elegant-pagination mt-6"
      color="primary"
    />

    <div class="write-button-container">
      <v-btn 
        color="primary" 
        @click="$emit('write')"
        class="write-btn"
        size="large"
        prepend-icon="mdi-pencil"
      >
        글쓰기
      </v-btn>
    </div>
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
.board-table-container {
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  border-radius: 16px;
  padding: 24px;
  margin: 16px 0;
}

.elegant-table {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.table-row {
  transition: all 0.3s ease;
  border-bottom: 1px solid #f0f2f5;
}

.table-row:hover {
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  color: white;
  cursor: pointer;
  transform: scale(1.01);
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.3);
}

.title-cell {
  font-weight: 500;
  padding: 16px !important;
}

.title-content {
  display: flex;
  align-items: center;
  gap: 8px;
}

.private-badge {
  font-size: 14px;
  opacity: 0.7;
}

.author-cell {
  padding: 16px !important;
  color: #666;
  font-weight: 500;
}

.date-cell {
  padding: 16px !important;
  color: #888;
  font-size: 14px;
}

.views-cell {
  padding: 16px !important;
}

.views-badge {
  background: linear-gradient(45deg, #667eea, #764ba2);
  color: white;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
}

.elegant-pagination {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}

.write-button-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 24px;
}

.write-btn {
  background: linear-gradient(45deg, #667eea, #764ba2) !important;
  border-radius: 12px;
  font-weight: 600;
  padding: 12px 24px;
  transition: all 0.3s ease;
}

.write-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
}
</style>