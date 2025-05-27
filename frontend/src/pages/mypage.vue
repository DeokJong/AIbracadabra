<template>
  <div class="mypage-container">
    <div class="page-header">
      <h1 class="page-title">
        <span class="title-icon">👤</span>
        마이페이지
      </h1>
      <p class="page-subtitle">개인정보 관리 및 활동 내역을 확인하세요</p>
    </div>

    <section class="content-section">
      <div class="tab-navigation">
        <button
          v-for="tab in tabs"
          :key="tab"
          @click="activeTab = tab"
          :class="['tab-button', { 'active': activeTab === tab }]"
        >
          <span class="tab-icon">{{ getTabIcon(tab) }}</span>
          <span class="tab-label">{{ tabLabels[tab] }}</span>
        </button>
      </div>
      
      <div class="tab-content">
        <div class="content-wrapper">
          <div v-if="activeTab === 'profile'" class="tab-panel">
            <MemberModify />
          </div>
          <div v-else-if="activeTab === 'boards'" class="tab-panel">
            <MyBoards />
          </div>
          <div v-else class="tab-panel">
            <MyComments />
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import MemberModify from '@/components/member/MemberModify.vue'
import MyBoards from '@/components/board/MyBoards.vue'
import MyComments from '@/components/comment/MyComments.vue'

const tabs = ['profile', 'boards', 'comments']
const tabLabels: Record<string, string> = {
  profile: '내 정보 수정',
  boards: '내가 쓴 게시글',
  comments: '내가 쓴 댓글',
}
const activeTab = ref('profile')

const navItems = tabs
const navLabels = tabLabels
const selectedNav = ref('profile')

function getTabIcon(tab: string): string {
  const icons: Record<string, string> = {
    profile: '⚙️',
    boards: '📝',
    comments: '💬'
  }
  return icons[tab] || '📄'
}
</script>

<style scoped>
.mypage-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 32px 16px;
}

.page-header {
  text-align: center;
  margin-bottom: 48px;
}

.page-title {
  font-size: 48px;
  font-weight: 800;
  color: white;
  margin: 0 0 16px 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  text-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.title-icon {
  font-size: 52px;
}

.page-subtitle {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.9);
  margin: 0;
  font-weight: 400;
}

.content-section {
  max-width: 1200px;
  margin: 0 auto;
  background: white;
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 0 25px 80px rgba(0, 0, 0, 0.15);
}

.tab-navigation {
  display: flex;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-bottom: 1px solid #e2e8f0;
}

.tab-button {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 24px 32px;
  border: none;
  background: transparent;
  font-size: 16px;
  font-weight: 600;
  color: #64748b;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
}

.tab-button::before {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 4px;
  background: linear-gradient(90deg, #667eea, #764ba2);
  border-radius: 2px;
  transition: all 0.3s ease;
  transform: translateX(-50%);
}

.tab-button:hover {
  background: rgba(102, 126, 234, 0.05);
  color: #667eea;
}

.tab-button.active {
  color: #667eea;
  background: white;
}

.tab-button.active::before {
  width: 80%;
}

.tab-icon {
  font-size: 20px;
}

.tab-label {
  font-weight: 600;
  letter-spacing: 0.5px;
}

.tab-content {
  background: white;
}

.content-wrapper {
  min-height: 600px;
}

.tab-panel {
  padding: 40px;
  animation: fadeInUp 0.5s ease;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .mypage-container {
    padding: 16px 8px;
  }
  
  .page-title {
    font-size: 36px;
    flex-direction: column;
    gap: 8px;
  }
  
  .tab-navigation {
    flex-direction: column;
  }
  
  .tab-button {
    padding: 16px 24px;
  }
  
  .tab-panel {
    padding: 24px 16px;
  }
}

@media (max-width: 480px) {
  .page-title {
    font-size: 28px;
  }
  
  .tab-button {
    padding: 12px 16px;
    font-size: 14px;
  }
  
  .tab-icon {
    font-size: 16px;
  }
}
</style>
