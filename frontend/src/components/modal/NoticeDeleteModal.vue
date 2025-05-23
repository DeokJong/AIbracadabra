<template>
  <v-dialog
    v-model="show"
    max-width="500"
    persistent
    scrollable
    transition="scale-transition"
    overlay-transition="fade-transition"
    @click:outside="onCancel"
  >
    <v-card>
      <!-- 모달 메인 컨텐트 영역-->
      <v-card-title class="text-h5">공지사항 삭제</v-card-title>
      <v-card-text>
        정말 삭제하시겠습니까?
      </v-card-text>


      <!-- 모달 하단부 닫는 부분 영역-->
      <v-card-actions class="justify-end">
        <v-btn text @click="onCancel">취소</v-btn>
        <v-btn class="bg-red" text @click="doWithdraw">예</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
// Modal 공통 정의 부분
import { ref, defineEmits,reactive } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'
import { useToast } from 'vue-toastification'
import { BoardSummary } from '@/components/notice/ItemTable.vue'



const route = useRoute()
const toast = useToast()

// const props = defineProps<{
//   title:string
// }>()
type BoardDetail = BoardSummary & {
  boardType: string
  content: string
  updatedDate: string
  visibility: string
  comments: Comment[]
}
const boardData = reactive<BoardDetail>({} as BoardDetail)


const emit = defineEmits<{
  (e: 'resolve'): void
  (e: 'close'): void
}>()

const show = ref(true)
const ANIM_DURATION = 1000  // ms

function onConfirm() {
  show.value = false
  setTimeout(() => emit('resolve'), ANIM_DURATION)
}
function onCancel() {
  show.value = false
  setTimeout(() => emit('close'), ANIM_DURATION)
}

// 모달 기능 부분
import router from '@/router';

// 여기서 삭제를 호출
const doWithdraw = async () => {
  const params = route.params as { bno: string }

  const bno = Number(params.bno)
  const detailApiUrl = `api/v1/board/${bno}`
  console.log(detailApiUrl)
  try {
    await axios.delete(detailApiUrl)
    toast.success('공지사항이 삭제되었습니다.')
  } catch (err) {
    toast.error('삭제에 실패했습니다.')
  } finally{
    show.value = false
    emit('resolve')
    router.push('/notice')
  }
}

</script>
