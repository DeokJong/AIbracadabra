<template>
  <div class="bottom-center-bar">
    <v-card flat class="pa-2 d-flex align-center" style="border-radius:32px;">
      <!-- 이전 버튼 -->
      <v-btn
        :disabled="markerMeta.pageNo <= 1"
        @click="goPage(markerMeta.pageNo - 1)"
        class="mx-1"
        small
      >
        이전
      </v-btn>
      <!-- 다음 버튼 -->
      <v-btn
        :disabled="markerMeta.end"
        @click="goPage(markerMeta.pageNo + 1)"
        class="mx-1"
        small
      >
        다음
      </v-btn>
    </v-card>
  </div>
</template>

<script setup lang="ts">
import { useKakaoMap } from '@/hooks/useKakaoMap'
import { storeToRefs } from 'pinia'

const kakaoMap = useKakaoMap()
const { contentSearch } = kakaoMap
const { markerMeta, lastSearchContentType  } = storeToRefs(kakaoMap)

function goPage(page: number) {
  contentSearch(lastSearchContentType.value, page )
}
</script>

<style scoped>
.bottom-center-bar {
  position: fixed;
  bottom: 16px;               /* 화면 하단에서 16px 위 */
  left: 50%;                  /* 가로 중앙 기준 */
  transform: translateX(-50%);/* 진짜 가로 중앙으로 이동 */
  z-index: 1000;
}
</style>
