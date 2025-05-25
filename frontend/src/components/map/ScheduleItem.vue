<template>
  <v-list-item class="schedule-item">
    <div class="schedule-title">
      <!-- 클릭 시 setContent 호출 -->
      <span class="clickable" @click.stop="clickItem">{{ item.title }}</span>
      <v-icon
        small
        class="title-remove"
        @click.stop="removeItem"
      >mdi-close</v-icon>
    </div>
    <div class="schedule-address">{{ item.address }}</div>
  </v-list-item>
</template>

<script setup lang="ts">
import { defineProps, defineEmits } from 'vue'
import { type FullDocument, useKakaoMap } from '@/hooks/useKakaoMap'

// 카카오맵 스토어에서 currentContent 가져오기
const { kakaoMapProps, setCurrentContent } = useKakaoMap()

const props = defineProps<{
  item: FullDocument
  index: number
}>()

const emit = defineEmits<{
  (e: 'remove', index: number): void
}>()

function removeItem() {
  emit('remove', props.index)
}

// 제목 클릭 시 currentContent 교체
function clickItem() {
  setCurrentContent(props.item)
  kakaoMapProps.lat = props.item.mapY
  kakaoMapProps.lng = props.item.mapX
}
</script>

<style scoped>
.schedule-item {
  display: grid;
  grid-template-rows: 2.4em auto;
  row-gap: 4px;

  box-sizing: border-box;
  width: 100%;

  padding: 12px 16px;
  margin-bottom: 8px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  background-color: #fafafa;
  transition: background-color 0.2s ease;
}
.schedule-item:hover {
  background-color: #f0f0f0;
}

.schedule-title {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  font-size: 1rem;
  font-weight: 600;

  overflow: hidden;
  white-space: normal;
}

.schedule-title span {
  flex: 1;
  margin-right: 8px;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  line-clamp: 2;
  overflow: hidden;
  text-overflow: ellipsis;

  word-break: break-word;
  overflow-wrap: anywhere;
}

.title-remove {
  flex-shrink: 0;
  cursor: pointer;
  color: #9e9e9e;
  transition: color 0.2s ease;
}
.title-remove:hover {
  color: #424242;
}

.schedule-address {
  /* 주소 행: 자동 높이, 줄바꿈 허용 */
  font-size: 0.875rem;
  color: #616161;
  line-height: 1.2em;
  white-space: normal;
  word-break: break-word;
  overflow-wrap: anywhere;
}
</style>

