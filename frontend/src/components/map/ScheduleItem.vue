<template>
  <v-list-item class="schedule-item">
    <v-list-item-content>
      <div class="schedule-title">
        <span>{{ item.title }}</span>
        <!-- 클릭 시 remove 이벤트 발생 -->
        <v-icon
          small
          class="remove-icon title-remove"
          @click.stop="removeItem"
        >mdi-close</v-icon>
      </div>
      <div class="schedule-address">{{ item.address }}</div>
    </v-list-item-content>
  </v-list-item>
</template>

<script setup lang="ts">
import { defineProps, defineEmits } from 'vue'
import type { FullDocument } from '@/hooks/useKakaoMap'

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

</script>

<style scoped>
.schedule-item {
  display: flex;
  flex-direction: column;
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
  align-items: center;
  justify-content: space-between;
  font-size: 1rem;
  font-weight: 600;
}
.schedule-address {
  font-size: 0.875rem;
  color: #616161;
  margin-top: 4px;
}
.title-remove {
  cursor: pointer;
  color: #9e9e9e;
  transition: color 0.2s ease;
}
.title-remove:hover {
  color: #424242;
}
</style>
