<template>
  <div>
    <v-btn icon class="toggle-btn elevation-6" :style="{ left: drawer ? width + 'px' : '0px' }"
      @click="drawer = !drawer">
      <v-icon>{{ drawer ? 'mdi-chevron-left' : 'mdi-chevron-right' }}</v-icon>
    </v-btn>

    <div class="sidebar elevation-6" :class="{ open: drawer }" :style="{ width: width + 'px' }">
      <div class="sidebar-content">
        <v-card v-if="currentContent.contentId" style="top: 60px;">
          <v-img v-if="currentContent.firstImage" :src="currentContent.firstImage" aspect-ratio="16/9" class="mb-4" />
          <v-card-title class="mb-2">
            {{ currentContent.title }}
          </v-card-title>
          <v-list dense>
            <v-list-item>
              <v-list-item-title>주소</v-list-item-title>
              <div style="color: gray">{{ currentContent.address }}</div>
            </v-list-item>
            <v-list-item>
              <v-list-item-title>상세 설명</v-list-item-title>
              <div style="color: gray">{{ currentContent.overview }}</div>
            </v-list-item>
            <v-list-item>
              <v-btn block color="info" @click="doAppend">담기</v-btn>
            </v-list-item>

            <v-list-item>
              <v-btn v-if="currentContent.contentsTypeId === '40' && currentContent.mno === userInfo.mno" block
                color="error" @click="removeHotPlace(Number(currentContent.contentId.replace('hotplace_','')))">핫 플레이스 취소</v-btn>
            </v-list-item>
          </v-list>
        </v-card>

      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useKakaoMap } from '@/hooks/useKakaoMap'
import { storeToRefs } from 'pinia'
import { usePlan } from '@/hooks/usePlan'
import { useAuth } from '@/hooks/useAuth'
import useHotPlace from '@/hooks/useHotPlace'


const props = defineProps({
  width: { type: [Number, String], default: 300 },
})
const { width } = props

// 열림/닫힘 상태
const drawer = ref(false)

const auth = useAuth()
const { userInfo } = storeToRefs(auth)
const { appendSchedule } = usePlan()
const { currentContent } = storeToRefs(useKakaoMap())
const { removeHotPlace } = useHotPlace()

const doAppend = () => {
  appendSchedule(currentContent.value)
}

// currentContent.contentId가 설정될 때 자동으로 열기
watch(
  () => currentContent.value.contentId,
  (id) => {
    if (id) drawer.value = true
  }
)
</script>

<style scoped>
.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh;
  transform: translateX(-100%);
  transition: transform 0.3s ease;
  background-color: white;
  overflow: auto;
  z-index: 999;
}

.sidebar.open {
  transform: translateX(0);
}

.sidebar-content {
  padding: 16;
}

.toggle-btn {
  position: fixed;
  top: 50%;
  left: 0;
  transition: left 0.3s ease;
  width: 16px;
  height: 64px;
  background: white;
  border-radius: 0px 8px 8px 0px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}
</style>
