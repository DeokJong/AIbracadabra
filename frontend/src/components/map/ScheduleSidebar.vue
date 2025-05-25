<template>
  <div>
    <!-- 토글 버튼 -->
    <v-btn
      icon
      class="toggle-btn elevation-6"
      :style="{ right: buttonRight }"
      @click="drawer = !drawer"
    >
      <v-icon>
        {{ drawer ? 'mdi-chevron-right' : 'mdi-chevron-left' }}
      </v-icon>
    </v-btn>

    <!-- 사이드바 -->
    <div
      class="sidebar elevation-6"
      :class="{ open: drawer }"
      :style="{ width: sidebarWidth }"
    >
      <div class="sidebar-content">
        <v-card flat style="top: 60px;">
          <v-card-title class="headline">나의 일정</v-card-title>

          <!-- 드래그 가능 리스트 -->
          <draggable
            v-model="planList"
            item-key="contentId"
            ghost-class="drag-ghost"
            handle=".schedule-item"
            @end="onDragEnd"
          >
            <template #item="{ element: it, index: idx }">
              <ScheduleItem
                :key="it.contentId"
                :item="it"
                :index="idx"
                @remove="onRemove"
              />
            </template>
          </draggable>

          <v-text-field
            v-model="currentPlan.title"
            label="일정 제목"
            class="mt-4"
          />
          <v-divider class="my-2" />
          <v-btn block color="success" class="mt-2" @click="savePlan">
            저장
          </v-btn>
        </v-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, computed } from 'vue'
import draggable from 'vuedraggable'
import ScheduleItem from '@/components/map/ScheduleItem.vue'
import { storeToRefs } from 'pinia'
import { usePlan } from '@/hooks/usePlan'

// props
const props = defineProps({
  width: { type: [Number, String], default: 300 },
})
const { width } = props

// Plan 스토어
const planStore = usePlan()
const { savePlan, removeSchedule } = planStore
const { currentPlan } = storeToRefs(planStore)

// schedules 양방향 바인딩
const planList = computed({
  get: () => currentPlan.value.schedules,
  set: (v: typeof currentPlan.value.schedules) => {
    currentPlan.value.schedules = v
  }
})

// 일정 삭제
function onRemove(idx: number) {
  removeSchedule(idx)
}

// 드래그 종료 핸들러
function onDragEnd(evt: { oldIndex: number; newIndex: number }) {
  console.log(`Moved from ${evt.oldIndex} to ${evt.newIndex}`)
}

// 사이드바 열림 상태
const drawer = ref(false)
watch(
  () => currentPlan.value.schedules.length,
  len => { drawer.value = len > 0 },
  { immediate: true }
)

// 스타일 계산
const formattedWidth = (val: number | string) => {
  if (typeof val === 'number') return `${val}px`
  if (/^\d+$/.test(val)) return `${val}px`
  return val
}
const sidebarWidth = computed(() => formattedWidth(width))
const buttonRight  = computed(() => (drawer.value ? sidebarWidth.value : '0px'))
</script>

<style scoped>
.sidebar {
  position: fixed;
  top: 0;
  right: 0;
  height: 100vh;
  transform: translateX(100%);
  transition: transform 0.3s ease;
  background: white;
  overflow: auto;
  z-index: 999;
}
.sidebar.open {
  transform: translateX(0);
}
.sidebar-content {
  padding: 16px;
}

.toggle-btn {
  position: fixed;
  top: 50%;
  right: 0;
  transition: right 0.3s ease;
  width: 16px;
  height: 64px;
  background: white;
  border-radius: 8px 0 0 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

/* 드래그 중일 때 */
.drag-ghost {
  opacity: 0.5;
  background: #e0f7fa;
}
</style>
