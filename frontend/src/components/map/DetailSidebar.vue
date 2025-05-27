<template>
  <div>
    <!-- 토글 버튼 (기존과 동일) -->
    <button 
      class="modern-toggle-btn" 
      :class="{ open: drawer }"
      :style="{ left: drawer ? width + 'px' : '0px' }"
      @click="drawer = !drawer"
    >
      <div class="toggle-icon">
        <v-icon :class="{ rotated: drawer }">
          {{ drawer ? 'mdi-chevron-left' : 'mdi-chevron-right' }}
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
    <div 
      class="modern-sidebar" 
      :class="{ open: drawer }" 
      :style="{ width: width + 'px' }"
    >
      <!-- 사이드바 헤더 -->
      <div class="sidebar-header">
        <div class="header-content">
          <h3 class="header-title">
            <span class="title-icon">📍</span>
            관광지 정보
          </h3>
          <button class="close-btn" @click="drawer = false">
            <v-icon>mdi-close</v-icon>
          </button>
        </div>
      </div>

      <!-- 사이드바 콘텐츠 -->
      <div class="sidebar-content">
        <div v-if="currentContent.contentId" class="content-card">
          <!-- 이미지 섹션 -->
          <div class="image-section">
            <div v-if="currentContent.firstImage" class="image-container">
              <img 
                :src="currentContent.firstImage" 
                :alt="currentContent.title"
                class="content-image" 
              />
              <div class="image-overlay">
                <div class="image-badge">
                  <span class="badge-icon">🏞️</span>
                  <span class="badge-text">관광지</span>
                </div>
              </div>
            </div>
            <div v-else class="no-image">
              <div class="no-image-icon">🏞️</div>
              <span class="no-image-text">이미지 없음</span>
            </div>
          </div>

          <!-- 콘텐츠 정보 -->
          <div class="content-info">
            <h2 class="content-title">{{ currentContent.title }}</h2>
            
            <div class="info-list">
              <!-- 주소 정보 -->
              <div class="info-item">
                <div class="info-label">
                  <span class="label-icon">📍</span>
                  주소
                </div>
                <div class="info-value address-info">
                  {{ currentContent.address || '주소 정보 없음' }}
                </div>
              </div>
              
              <!-- 상세 설명 - 가독성 개선 -->
              <div class="info-item description-section">
                <div class="info-label">
                  <span class="label-icon">📋</span>
                  상세 설명
                  <button 
                    class="expand-btn"
                    @click="isDescriptionExpanded = !isDescriptionExpanded"
                    v-if="currentContent.overview && currentContent.overview.length > 100"
                  >
                    {{ isDescriptionExpanded ? '접기' : '더보기' }}
                  </button>
                </div>
                
                <div class="description-container">
                  <div 
                    class="info-value description"
                    :class="{ expanded: isDescriptionExpanded }"
                  >
                    <div class="description-content">
                      {{ currentContent.overview || '상세 설명이 없습니다.' }}
                    </div>
                  </div>
                  
                  <!-- 그라디언트 마스크 (더보기 효과) -->
                  <div 
                    v-if="!isDescriptionExpanded && currentContent.overview && currentContent.overview.length > 100"
                    class="description-gradient"
                  ></div>
                </div>
              </div>
            </div>

            <!-- 액션 버튼들 -->
            <div class="action-buttons">
              <button class="action-btn primary" @click="doAppend">
                <span class="btn-icon">➕</span>
                <span class="btn-text">여행 계획에 추가</span>
              </button>
              
              <button 
                v-if="currentContent.contentsTypeId === '40' && currentContent.mno === userInfo.mno" 
                class="action-btn danger"
                @click="removeHotPlace(Number(currentContent.contentId.replace('hotplace_','')))"
              >
                <span class="btn-icon">🗑️</span>
                <span class="btn-text">핫 플레이스 취소</span>
              </button>
            </div>
          </div>
        </div>

        <!-- 빈 상태 -->
        <div v-else class="empty-state">
          <div class="empty-icon">🗺️</div>
          <h3 class="empty-title">관광지를 선택해주세요</h3>
          <p class="empty-description">
            지도에서 관광지를 클릭하면<br>
            자세한 정보를 확인할 수 있습니다
          </p>
        </div>
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
  width: { type: [Number, String], default: 380 },
})
const { width } = props

// 열림/닫힘 상태
const drawer = ref(false)
// 상세설명 확장 상태 추가
const isDescriptionExpanded = ref(false)

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
    if (id) {
      drawer.value = true
      isDescriptionExpanded.value = false // 새 콘텐츠 시 설명 접기
    }
  }
)
</script>

<style scoped>
/* 기존 스타일은 동일하게 유지하고 아래 부분만 추가/수정 */

/* 상세 설명 섹션 개선 */
.description-section {
  position: relative;
}

.info-label {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.5rem;
  font-weight: 600;
  color: #4B6DFF;
  font-size: 0.9rem;
  margin-bottom: 1rem;
}

.expand-btn {
  background: none;
  border: 1px solid #4B6DFF;
  color: #4B6DFF;
  padding: 0.25rem 0.75rem;
  border-radius: 12px;
  font-size: 0.8rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.expand-btn:hover {
  background: #4B6DFF;
  color: white;
}

.description-container {
  position: relative;
}

.info-value.description {
  color: #555;
  line-height: 1.7;
  background: #f8f9fa;
  padding: 1.5rem;
  border-radius: 16px;
  border-left: 4px solid #4B6DFF;
  font-size: 0.95rem;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.info-value.description:not(.expanded) {
  max-height: 120px;
}

.info-value.description.expanded {
  max-height: none;
}

.description-content {
  white-space: pre-wrap;
  word-break: break-word;
}

.description-gradient {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 40px;
  background: linear-gradient(
    to bottom,
    rgba(248, 249, 250, 0) 0%,
    rgba(248, 249, 250, 0.8) 50%,
    rgba(248, 249, 250, 1) 100%
  );
  pointer-events: none;
}

/* 주소 정보 스타일 개선 */
.info-value.address-info {
  color: #666;
  line-height: 1.6;
  background: #e8f4ff;
  padding: 1rem 1.25rem;
  border-radius: 12px;
  border-left: 3px solid #4B6DFF;
  font-size: 0.9rem;
  font-weight: 500;
}

/* 토글 버튼 스타일 (기존과 동일) */
.modern-toggle-btn {
  position: fixed;
  top: 50%;
  left: 0;
  width: 48px;
  height: 90px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-left: none;
  border-radius: 0 20px 20px 0;
  box-shadow: 
    0 8px 30px rgba(0, 0, 0, 0.15),
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

.modern-toggle-btn::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, #4B6DFF 0%, #6DC7FC 100%);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.modern-toggle-btn:hover::before {
  opacity: 0.1;
}

.modern-toggle-btn:hover {
  transform: translateY(-50%) translateX(5px);
  box-shadow: 
    0 12px 40px rgba(0, 0, 0, 0.2),
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

/* 나머지 기존 스타일들 동일 유지... */
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

.modern-sidebar {
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-right: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 
    4px 0 30px rgba(0, 0, 0, 0.1),
    0 0 0 1px rgba(255, 255, 255, 0.1) inset;
  transform: translateX(-100%);
  transition: transform 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  z-index: 1000;
  display: flex;
  flex-direction: column;
}

.modern-sidebar.open {
  transform: translateX(0);
}

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

.sidebar-content {
  flex: 1;
  overflow-y: auto;
  padding: 2rem;
}

.content-card {
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.3);
  animation: slideIn 0.5s ease-out;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.image-section {
  position: relative;
}

.image-container {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.content-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.content-image:hover {
  transform: scale(1.05);
}

.image-overlay {
  position: absolute;
  top: 1rem;
  right: 1rem;
}

.image-badge {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 600;
  color: #333;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.no-image {
  height: 200px;
  background: linear-gradient(135deg, #f8f9fa, #e9ecef);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #6c757d;
}

.no-image-icon {
  font-size: 3rem;
  margin-bottom: 0.5rem;
  opacity: 0.5;
}

.content-info {
  padding: 2rem;
}

.content-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 1.5rem;
  line-height: 1.3;
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 2rem;
  margin-bottom: 2rem;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.action-buttons {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.action-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.75rem;
  padding: 1rem 1.5rem;
  border: none;
  border-radius: 14px;
  font-weight: 600;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.action-btn.primary {
  background: linear-gradient(135deg, #4B6DFF 0%, #6DC7FC 100%);
  color: white;
  box-shadow: 0 4px 15px rgba(75, 109, 255, 0.3);
}

.action-btn.primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(75, 109, 255, 0.4);
}

.action-btn.danger {
  background: linear-gradient(135deg, #FF6B6B 0%, #FF3B3B 100%);
  color: white;
  box-shadow: 0 4px 15px rgba(255, 107, 107, 0.3);
}

.action-btn.danger:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(255, 107, 107, 0.4);
}

.empty-state {
  text-align: center;
  padding: 3rem 2rem;
  color: #6c757d;
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
  opacity: 0.5;
}

.empty-title {
  font-size: 1.3rem;
  font-weight: 600;
  color: #495057;
  margin-bottom: 1rem;
}

.empty-description {
  line-height: 1.6;
  margin: 0;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .modern-sidebar {
    width: 100vw !important;
  }
  
  .info-value.description:not(.expanded) {
    max-height: 100px;
  }
  
  .expand-btn {
    font-size: 0.75rem;
    padding: 0.2rem 0.6rem;
  }
}
</style>
