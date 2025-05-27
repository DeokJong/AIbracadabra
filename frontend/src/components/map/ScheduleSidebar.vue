<template>
  <div>
    <!-- 토글 버튼 -->
    <button 
      class="modern-toggle-btn right" 
      :class="{ open: drawer }"
      :style="{ right: buttonRight }" 
      @click="drawer = !drawer"
    >
      <div class="toggle-icon">
        <v-icon :class="{ rotated: drawer }">
          {{ drawer ? 'mdi-chevron-right' : 'mdi-chevron-left' }}
        </v-icon>
      </div>
      <div class="toggle-pulse" :class="{ active: !drawer }"></div>
    </button>

    <!-- 사이드바 오버레이 -->
    <div 
      v-if="drawer" 
      class="sidebar-overlay" 
      @click="drawer = false"
    ></div>

    <!-- 메인 사이드바 -->
    <div class="modern-sidebar right" :class="{ open: drawer }" :style="{ width: sidebarWidth }">
      <!-- 사이드바 헤더 -->
      <div class="sidebar-header">
        <div class="header-content">
          <h3 class="header-title">
            <span class="title-icon">📅</span>
            여행 계획
          </h3>
          <button class="close-btn" @click="drawer = false">
            <v-icon>mdi-close</v-icon>
          </button>
        </div>
      </div>

      <!-- 사이드바 콘텐츠 -->
      <div class="sidebar-content">
        <!-- 새 일정 버튼 -->
        <div class="action-section">
          <button class="new-plan-btn" @click="addNewPlan">
            <span class="btn-icon">➕</span>
            <span class="btn-text">새 일정 추가</span>
          </button>
        </div>

        <!-- 현재 플랜 정보 -->
        <div class="plan-info-card">
          <div class="plan-header">
            <h2 class="plan-title">{{ currentPlan.title || '새 일정' }}</h2>
            
            <!-- 페이징 컨트롤 -->
            <div class="pagination-controls">
              <button 
                class="page-btn prev" 
                @click="prevPage" 
                :disabled="currentPage === 1"
              >
                <v-icon>mdi-chevron-left</v-icon>
              </button>
              <span class="page-indicator">{{ currentPage }} / {{ totalPages }}</span>
              <button 
                class="page-btn next" 
                @click="nextPage" 
                :disabled="currentPage === totalPages"
              >
                <v-icon>mdi-chevron-right</v-icon>
              </button>
            </div>
          </div>

          <div class="plan-divider"></div>

          <!-- 스케줄 리스트 -->
          <div class="schedule-section">
            <div class="schedule-header">
              <h4 class="schedule-title">
                <span class="schedule-icon">📍</span>
                여행지 목록 ({{ bindingPlan.length }}개)
              </h4>
            </div>

            <div class="schedule-list">
              <draggable 
                v-model="bindingPlan" 
                item-key="contentId" 
                ghost-class="drag-ghost" 
                handle=".drag-handle"
                class="draggable-container"
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

              <!-- 빈 상태 -->
              <div v-if="bindingPlan.length === 0" class="empty-schedule">
                <div class="empty-icon">🗺️</div>
                <p class="empty-text">아직 추가된 여행지가 없습니다</p>
                <p class="empty-subtext">지도에서 관광지를 선택해 추가해보세요</p>
              </div>
            </div>
          </div>

          <!-- 플랜 제목 수정 -->
          <div class="title-edit-section">
            <div class="input-group">
              <span class="input-icon">✏️</span>
              <input 
                v-model="currentPlan.title" 
                placeholder="일정 제목을 입력하세요"
                class="title-input"
              />
            </div>
          </div>

          <!-- 액션 버튼들 -->
          <div class="plan-actions">
            <button class="plan-btn view" @click="setupMarker">
              <span class="btn-icon">👁️</span>
              <span class="btn-text">지도에서 보기</span>
            </button>
            
            <button class="plan-btn save" @click="savePlan">
              <span class="btn-icon">💾</span>
              <span class="btn-text">계획 저장</span>
            </button>
            
            <button class="plan-btn delete" @click="removeAndInitPage">
              <span class="btn-icon">🗑️</span>
              <span class="btn-text">계획 삭제</span>
            </button>
          </div>
        </div>
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

// 기존 로직들은 동일하게 유지
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
  width: { type: [Number, String], default: 380 },
})

const sidebarWidth = computed(() => formattedWidth(props.width))
const buttonRight = computed(() => (drawer.value ? sidebarWidth.value : '0px'))
</script>

<style scoped>
/* 오른쪽 토글 버튼 */
.modern-toggle-btn.right {
  position: fixed;
  top: 50%;
  right: 0;
  width: 48px;
  height: 90px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-right: none;
  border-radius: 20px 0 0 20px;
  box-shadow: 
    -8px 0 30px rgba(0, 0, 0, 0.15),
    0 0 0 1px rgba(255, 255, 255, 0.1) inset;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 1100;
  transform: translateY(-50%);
  overflow: hidden;
}

.modern-toggle-btn.right::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, #4B6DFF 0%, #6DC7FC 100%);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.modern-toggle-btn.right:hover::before {
  opacity: 0.1;
}

.modern-toggle-btn.right:hover {
  transform: translateY(-50%) translateX(-5px);
  box-shadow: 
    -12px 0 40px rgba(0, 0, 0, 0.2),
    0 0 0 1px rgba(255, 255, 255, 0.2) inset;
}

.toggle-icon {
  position: relative;
  z-index: 2;
  transition: all 0.3s ease;
}

.toggle-icon v-icon {
  font-size: 1.5rem;
  color: #4B6DFF;
  transition: all 0.3s ease;
}

.toggle-icon.rotated v-icon {
  transform: rotate(180deg);
}

.modern-toggle-btn:hover .toggle-icon v-icon {
  color: #2a4bd7;
  transform: scale(1.1);
}

.toggle-pulse {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 24px;
  height: 24px;
  background: #4B6DFF;
  border-radius: 50%;
  transform: translate(-50%, -50%) scale(0);
  opacity: 0;
}

.toggle-pulse.active {
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0% {
    transform: translate(-50%, -50%) scale(0);
    opacity: 1;
  }
  70% {
    transform: translate(-50%, -50%) scale(1);
    opacity: 0.5;
  }
  100% {
    transform: translate(-50%, -50%) scale(1.5);
    opacity: 0;
  }
}

/* 오버레이 */
.sidebar-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(2px);
  z-index: 999;
  animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

/* 오른쪽 사이드바 */
.modern-sidebar.right {
  position: fixed;
  top: 0;
  right: 0;
  height: 100vh;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-left: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 
    -4px 0 30px rgba(0, 0, 0, 0.1),
    0 0 0 1px rgba(255, 255, 255, 0.1) inset;
  transform: translateX(100%);
  transition: transform 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  z-index: 1000;
  display: flex;
  flex-direction: column;
}

.modern-sidebar.right.open {
  transform: translateX(0);
}

/* 사이드바 헤더 */
.sidebar-header {
  background: linear-gradient(135deg, #4B6DFF 0%, #6DC7FC 100%);
  color: white;
  padding: 1.5rem;
  position: relative;
  overflow: hidden;
}

.sidebar-header::before {
  content: '';
  position: absolute;
  inset: 0;
  background: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.1'%3E%3Ccircle cx='30' cy='30' r='4'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  z-index: 2;
}

.header-title {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  font-size: 1.3rem;
  font-weight: 700;
  margin: 0;
}

.title-icon {
  font-size: 1.4rem;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));
}

.close-btn {
  width: 36px;
  height: 36px;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 12px;
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.close-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: scale(1.05);
}

/* 사이드바 콘텐츠 */
.sidebar-content {
  flex: 1;
  overflow-y: auto;
  padding: 2rem;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.sidebar-content::-webkit-scrollbar {
  width: 6px;
}

.sidebar-content::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.05);
  border-radius: 3px;
}

.sidebar-content::-webkit-scrollbar-thumb {
  background: rgba(75, 109, 255, 0.3);
  border-radius: 3px;
}

/* 새 일정 버튼 */
.action-section {
  margin-bottom: 1rem;
}

.new-plan-btn {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.75rem;
  padding: 1rem 1.5rem;
  background: linear-gradient(135deg, #10B981 0%, #059669 100%);
  border: none;
  border-radius: 16px;
  color: white;
  font-weight: 600;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(16, 185, 129, 0.3);
}

.new-plan-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(16, 185, 129, 0.4);
}

.btn-icon {
  font-size: 1.1rem;
}

/* 플랜 정보 카드 */
.plan-info-card {
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.3);
  flex: 1;
  display: flex;
  flex-direction: column;
}

.plan-header {
  padding: 1.5rem;
  background: linear-gradient(135deg, #f8f9fa, #e9ecef);
}

.plan-title {
  font-size: 1.3rem;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 1rem;
  text-align: center;
}

.pagination-controls {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
}

.page-btn {
  width: 36px;
  height: 36px;
  border: 1px solid #dee2e6;
  background: white;
  border-radius: 12px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  color: #6c757d;
}

.page-btn:hover:not(:disabled) {
  background: #4B6DFF;
  color: white;
  border-color: #4B6DFF;
  transform: scale(1.05);
}

.page-btn:disabled {
  opacity: 0.3;
  cursor: not-allowed;
}

.page-indicator {
  font-weight: 600;
  color: #4B6DFF;
  min-width: 60px;
  text-align: center;
}

.plan-divider {
  height: 2px;
  background: linear-gradient(90deg, #4B6DFF 0%, #6DC7FC 100%);
}

/* 스케줄 섹션 */
.schedule-section {
  flex: 1;
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
}

.schedule-header {
  margin-bottom: 1rem;
}

.schedule-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1rem;
  font-weight: 600;
  color: #4B6DFF;
  margin: 0;
}

.schedule-icon {
  font-size: 1.1rem;
}

.schedule-list {
  flex: 1;
  min-height: 200px;
}

.draggable-container {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.empty-schedule {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  text-align: center;
  color: #6c757d;
  min-height: 150px;
}

.empty-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
  opacity: 0.5;
}

.empty-text {
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: #495057;
}

.empty-subtext {
  font-size: 0.9rem;
  opacity: 0.8;
  margin: 0;
}

/* 제목 수정 섹션 */
.title-edit-section {
  padding: 1.5rem;
  border-top: 1px solid #f0f0f0;
}

.input-group {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 12px;
  padding: 0.5rem;
  transition: all 0.3s ease;
}

.input-group:focus-within {
  border-color: #4B6DFF;
  box-shadow: 0 0 0 3px rgba(75, 109, 255, 0.1);
}

.input-icon {
  font-size: 1.1rem;
  margin-left: 0.5rem;
}

.title-input {
  flex: 1;
  border: none;
  background: transparent;
  padding: 0.75rem;
  font-size: 1rem;
  color: #333;
  outline: none;
  font-family: inherit;
}

.title-input::placeholder {
  color: #999;
}

/* 플랜 액션 버튼들 */
.plan-actions {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  padding: 1.5rem;
  border-top: 1px solid #f0f0f0;
}

.plan-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.75rem;
  padding: 0.875rem 1.25rem;
  border: none;
  border-radius: 12px;
  font-weight: 600;
  font-size: 0.95rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.plan-btn.view {
  background: linear-gradient(135deg, #6366F1 0%, #8B5CF6 100%);
  color: white;
  box-shadow: 0 3px 12px rgba(99, 102, 241, 0.3);
}

.plan-btn.save {
  background: linear-gradient(135deg, #10B981 0%, #059669 100%);
  color: white;
  box-shadow: 0 3px 12px rgba(16, 185, 129, 0.3);
}

.plan-btn.delete {
  background: linear-gradient(135deg, #EF4444 0%, #DC2626 100%);
  color: white;
  box-shadow: 0 3px 12px rgba(239, 68, 68, 0.3);
}

.plan-btn:hover {
  transform: translateY(-2px);
}

.plan-btn.view:hover {
  box-shadow: 0 6px 20px rgba(99, 102, 241, 0.4);
}

.plan-btn.save:hover {
  box-shadow: 0 6px 20px rgba(16, 185, 129, 0.4);
}

.plan-btn.delete:hover {
  box-shadow: 0 6px 20px rgba(239, 68, 68, 0.4);
}

/* 드래그 효과 */
.drag-ghost {
  opacity: 0.5;
  transform: rotate(5deg);
  background: linear-gradient(135deg, #e3f2fd, #bbdefb);
  border: 2px dashed #4B6DFF;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .modern-sidebar.right {
    width: 100vw !important;
  }
  
  .sidebar-content {
    padding: 1.5rem;
  }
  
  .plan-info-card {
    margin: 0;
  }
  
  .plan-header,
  .schedule-section,
  .title-edit-section,
  .plan-actions {
    padding: 1rem;
  }
}
</style>
