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
      <v-card-title class="text-h5">회원 탈퇴</v-card-title>
      <v-card-text>
        정말 탈퇴하시겠습니까?
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
import { ref, defineEmits } from 'vue'

// const props = defineProps<{
//   title:string
// }>()

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
import { useAuth } from '@/hooks/useAuth';
import router from '@/router';

const { withdraw } = useAuth()

const doWithdraw = async () => {
  const ok = await withdraw()
  if(ok) {
    show.value = false
    emit('resolve')
    router.push('/')
  }
}

</script>
