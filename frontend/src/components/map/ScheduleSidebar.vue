<template>
  <div>
    <!-- 토글 버튼 -->
    <v-btn icon class="toggle-btn elevation-6" :style="{ right: buttonRight }" @click="drawer = !drawer">
      <v-icon>{{ drawer ? 'mdi-chevron-right' : 'mdi-chevron-left' }}</v-icon>
    </v-btn>

    <!-- 사이드바 -->
    <div class="sidebar elevation-6" :class="{ open: drawer }" :style="{ width: sidebarWidth }">
      <div class="sidebar-content">
        <v-card flat style="top: 60px;">
          <v-btn block color="primary" class="mb-2" @click="addNewPlan">
            새 일정
          </v-btn>

          <!-- 1) 현재 Plan 제목 -->
          <v-card-title class="headline">
            {{ currentPlan.title }}
          </v-card-title>

          <!-- 2) 이전/다음 페이징 -->
          <div class="d-flex justify-space-between align-center mb-4">
            <v-btn small @click="prevPage" :disabled="currentPage === 1">
              이전
            </v-btn>
            <span>{{ currentPage }} / {{ totalPages }}</span>
            <v-btn small @click="nextPage" :disabled="currentPage === totalPages">
              다음
            </v-btn>
          </div>

          <v-divider class="my-2" />

          <!-- 3) 선택된 플랜의 schedules 드래그 리스트 -->
          <draggable v-model="bindingPlan" item-key="contentId" ghost-class="drag-ghost" handle=".schedule-item">
            <template #item="{ element: it, index: idx }">
              <ScheduleItem :key="it.contentId" :item="it" :index="idx" @remove="onRemove" />
            </template>
          </draggable>

          <v-text-field v-model="currentPlan.title" label="일정 제목" class="mt-4" />
          <v-divider class="my-2" />
          <v-btn block color="info" class="mt-2" @click="setupMarker">
            보기
          </v-btn>
          <v-btn block color="success" class="mt-2" @click="savePlan">
            저장
          </v-btn>
          <v-btn block color="error" class="mt-2" @click="removeAndInitPage">
            제거
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
import { Plan, usePlan } from '@/hooks/usePlan'
import { ContentTypeImageResolver, useKakaoMap } from '@/hooks/useKakaoMap'

const plan = usePlan()
const { savePlan, removePlan, removeSchedule } = plan
const { currentPlan, myPlans } = storeToRefs(plan)

const kakaoMap = useKakaoMap()
const { markerProps, kakaoMapProps } = storeToRefs(kakaoMap)

const drawer = ref(false)
const currentPage = ref(1)
const totalPages = computed(() => myPlans.value.length ? myPlans.value.length : 1)

watch(
  () => myPlans.value.length,
  len => {
    if (len === 0) {
      currentPage.value = 1
      plan.clearPlan()
    } else if (currentPage.value > len) {
      currentPage.value = len
    }
  },
  { immediate: true }
)

watch(
  currentPage,
  page => {
    const len = myPlans.value.length
    if (len === 0) {
      plan.clearPlan()
      return
    }
    const idx = page - 1
    if (idx >= 0 && idx < len) {
      currentPlan.value = myPlans.value[idx]
    }
  },
  { immediate: true }
)

watch(
  () => currentPlan.value.pno,
  pno => {
    if (!pno) return

    const idx = myPlans.value.findIndex(p => p.pno === pno)
    if (idx === -1) {
      myPlans.value.push({ ...currentPlan.value })
      currentPage.value = myPlans.value.length
    } else {
      currentPage.value = idx + 1
    }
  }
)

watch(
  () => currentPlan.value.schedules.length,
  len => { drawer.value = drawer.value ? drawer.value : len > 0 },
  { immediate: true }
)

function prevPage() {
  if (currentPage.value > 1) currentPage.value--
}
function nextPage() {
  if (currentPage.value < totalPages.value) currentPage.value++
}

const bindingPlan = computed<Plan['schedules']>({
  get() { return currentPlan.value.schedules },
  set(v) { currentPlan.value.schedules = v }
})

function onRemove(idx: number) {
  removeSchedule(idx)
}

async function removeAndInitPage() {
  await removePlan()
  currentPage.value = 1
  currentPlan.value = myPlans.value[0]
}

function addNewPlan() {
  const newPlan: Plan = {
    title: '새 일정',
    pno: 0,
    schedules: []
  }
  myPlans.value.push(newPlan)
  currentPage.value = myPlans.value.length
}

function setupMarker() {
  markerProps.value = []
  if (currentPlan.value.schedules.length) {
    const data = currentPlan.value.schedules[0]
    kakaoMapProps.value.lng = data.mapX
    kakaoMapProps.value.lat = data.mapY
  }
  currentPlan.value.schedules.forEach((ele) => {
    markerProps.value.push({
      lng: ele.mapX,
      lat: ele.mapY,
      image: {
        imageSrc: `/marker/${ContentTypeImageResolver(ele.contentsTypeId)}`,
        imageHeight: 32,
        imageWidth: 32,
      },
      clickable: true,
      info: {
        contentId: ele.contentId,
      },
    })
  })
}


const formattedWidth = (val: number | string) => {
  if (typeof val === 'number') return `${val}px`
  if (/^\d+$/.test(val)) return `${val}px`
  return val
}
const props = defineProps({
  width: { type: [Number, String], default: 300 },
})
const sidebarWidth = computed(() => formattedWidth(props.width))
const buttonRight = computed(() => (drawer.value ? sidebarWidth.value : '0px'))
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
