<template>
  <div class="modern-schedule-item schedule-item" @click.stop="clickItem">
    <div class="item-content">
      <!-- 아이템 헤더 -->
      <div class="item-header">
        <div class="item-indicator">
          <!-- 이것만 수정하면됨-->
           <v-icon> {{ IconType }}</v-icon>
          <span class="item-number">{{ index + 1 }}</span>
        </div>
        <button class="remove-btn" @click.stop="removeItem">
          <v-icon>mdi-close</v-icon>
        </button>
      </div>

      <!-- 제목과 주소 -->
      <div class="item-info">
        <h4 class="item-title">{{ item.title }}</h4>
        <p class="item-address">
          <span class="address-icon">📍</span>
          {{ item.address || '주소 정보 없음' }}
        </p>
      </div>

      <!-- 드래그 핸들 - 클래스명 중요! -->
      <div class="drag-handle">
        <div class="drag-indicator">
          <span class="drag-dot"></span>
          <span class="drag-dot"></span>
          <span class="drag-dot"></span>
          <span class="drag-dot"></span>
          <span class="drag-dot"></span>
          <span class="drag-dot"></span>
        </div>
      </div>
    </div>

    <!-- 호버 효과 -->
    <div class="item-overlay"></div>
  </div>
</template>

<script setup lang="ts">
import { defineProps, defineEmits, computed } from 'vue'
import { type FullDocument, useKakaoMap, ContentTypeFabIconResolver } from '@/hooks/useKakaoMap'

const { kakaoMapProps, setCurrentContent } = useKakaoMap()

const props = defineProps<{
  item: FullDocument
  index: number
}>()


const IconType = computed(() => ContentTypeFabIconResolver(props.item.contentsTypeId)) 
const emit = defineEmits<{
  (e: 'remove', index: number): void
}>()

function removeItem() {
  emit('remove', props.index)
}

function clickItem() {
  setCurrentContent(props.item)
  kakaoMapProps.lat = props.item.mapY
  kakaoMapProps.lng = props.item.mapX
}
</script>

<style scoped>
.modern-schedule-item {
  position: relative;
  background: white;
  border: 1px solid rgba(75, 109, 255, 0.1);
  border-radius: 16px;
  padding: 1.25rem;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  animation: slideIn 0.4s ease-out;
}

.modern-schedule-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(75, 109, 255, 0.15);
  border-color: rgba(75, 109, 255, 0.3);
}

.modern-schedule-item:active {
  transform: translateY(0);
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.item-content {
  position: relative;
  z-index: 2;
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.item-indicator {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.indicator-dot {
  width: 8px;
  height: 8px;
  background: linear-gradient(135deg, #4B6DFF, #6DC7FC);
  border-radius: 50%;
  animation: pulse-dot 2s ease-in-out infinite;
}

@keyframes pulse-dot {
  0%, 100% { transform: scale(1); opacity: 1; }
  50% { transform: scale(1.2); opacity: 0.8; }
}

.item-number {
  background: linear-gradient(135deg, #4B6DFF, #6DC7FC);
  color: white;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.8rem;
  font-weight: 600;
  box-shadow: 0 2px 6px rgba(75, 109, 255, 0.3);
}

.remove-btn {
  width: 28px;
  height: 28px;
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid rgba(239, 68, 68, 0.2);
  border-radius: 8px;
  color: #EF4444;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  opacity: 0.7;
}

.remove-btn:hover {
  background: #EF4444;
  color: white;
  opacity: 1;
  transform: scale(1.1);
}

.item-info {
  margin-bottom: 1rem;
}

.item-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 0.75rem 0;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.item-address {
  display: flex;
  align-items: flex-start;
  gap: 0.5rem;
  font-size: 0.9rem;
  color: #6c757d;
  line-height: 1.4;
  margin: 0;
}

.address-icon {
  font-size: 1rem;
  flex-shrink: 0;
  margin-top: 0.1rem;
}

/* 드래그 핸들 개선 */
.drag-handle {
  position: absolute;
  top: 50%;
  right: 1rem;
  transform: translateY(-50%);
  cursor: grab;
  padding: 0.5rem;
  border-radius: 8px;
  transition: all 0.3s ease;
  opacity: 0.4;
}

.drag-handle:hover {
  opacity: 1;
  background: rgba(75, 109, 255, 0.1);
}

.drag-handle:active {
  cursor: grabbing;
}

.modern-schedule-item:hover .drag-handle {
  opacity: 0.8;
}

.drag-indicator {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 3px;
  width: 12px;
  height: 12px;
}

.drag-dot {
  width: 4px;
  height: 4px;
  background: #4B6DFF;
  border-radius: 50%;
  transition: all 0.3s ease;
}

.drag-handle:hover .drag-dot {
  background: #2a4bd7;
  transform: scale(1.2);
}

.item-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, #4B6DFF, #6DC7FC);
  opacity: 0;
  transition: opacity 0.3s ease;
  pointer-events: none;
}

.modern-schedule-item:hover .item-overlay {
  opacity: 0.03;
}

/* 드래그 중일 때 스타일 - 클래스명 수정 */
.drag-ghost {
  opacity: 0.6;
  transform: rotate(2deg) scale(1.02);
  background: linear-gradient(135deg, #e3f2fd, #bbdefb) !important;
  border: 2px dashed #4B6DFF !important;
  box-shadow: 0 8px 25px rgba(75, 109, 255, 0.3) !important;
}

.sortable-chosen {
  transform: scale(1.02);
  box-shadow: 0 8px 25px rgba(75, 109, 255, 0.2);
}

.sortable-drag {
  opacity: 1;
  transform: rotate(-2deg) scale(1.05);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.2);
  z-index: 1000;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .modern-schedule-item {
    padding: 1rem;
  }
  
  .item-title {
    font-size: 1rem;
  }
  
  .item-address {
    font-size: 0.85rem;
  }
  
  .item-number {
    width: 20px;
    height: 20px;
    font-size: 0.75rem;
  }
  
  .remove-btn {
    width: 24px;
    height: 24px;
  }
  
  .drag-handle {
    right: 0.5rem;
  }
}
</style>
