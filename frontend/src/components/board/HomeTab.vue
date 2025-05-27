<template>
  <div class="modern-tabs-container">
    <div class="tabs-header">
      <h2 class="section-title">
        <span class="title-icon">🔥</span>
        커뮤니티 하이라이트
      </h2>
      <div class="tab-buttons">
        <button
          v-for="tab in tabs"
          :key="tab.name"
          :class="['tab-button', { active: activeTab === tab.name }]"
          @click="activeTab = tab.name"
        >
          <span class="tab-icon">{{ tab.icon }}</span>
          {{ tab.label }}
        </button>
      </div>
    </div>

    <div class="tab-content-wrapper">
      <transition name="fade" mode="out-in">
        <component 
          :is="activeTabComponent" 
          :boardType="currentBoardType"
          :title="currentTabTitle"
          :key="activeTab"
        />
      </transition>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import PopularBoard from '@/components/board/PopularBoard.vue'

const activeTab = ref('PopularBoardBoard')

const tabs = [
  { 
    name: 'PopularBoardBoard', 
    label: '인기 게시글',
    icon: '📝'
  },
  { 
    name: 'PopularBoardNotice', 
    label: '최근 공지사항',
    icon: '📢'
  },
  { 
    name: 'PopularBoardQna', 
    label: '최근 Q&A',
    icon: '💬'
  },
]

const activeTabComponent = computed(() => PopularBoard)

const currentBoardType = computed(() => {
  if (activeTab.value === 'PopularBoardBoard') return 'board'
  if (activeTab.value === 'PopularBoardNotice') return 'notice'
  if (activeTab.value === 'PopularBoardQna') return 'qna'
})

const currentTabTitle = computed(() => {
  const tab = tabs.find(t => t.name === activeTab.value)
  return tab ? tab.label : ''
})
</script>

<style scoped>
.modern-tabs-container {
  max-width: 1200px;
  margin: 0 auto;
  background: white;
  border-radius: 20px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.tabs-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 2rem;
  color: white;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1.8rem;
  font-weight: 700;
  margin-bottom: 1.5rem;
  margin: 0 0 1.5rem 0;
}

.title-icon {
  font-size: 2rem;
}

.tab-buttons {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.tab-button {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.8rem 1.5rem;
  background: rgba(255, 255, 255, 0.1);
  border: 2px solid rgba(255, 255, 255, 0.2);
  border-radius: 50px;
  color: white;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.tab-button:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

.tab-button.active {
  background: white;
  color: #667eea;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.tab-icon {
  font-size: 1.2rem;
}

.tab-content-wrapper {
  padding: 2rem;
  min-height: 400px;
}

/* 전환 애니메이션 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@media (max-width: 768px) {
  .tabs-header {
    padding: 1.5rem;
  }
  
  .section-title {
    font-size: 1.5rem;
  }
  
  .tab-buttons {
    justify-content: center;
  }
  
  .tab-button {
    padding: 0.6rem 1rem;
    font-size: 0.9rem;
  }
  
  .tab-content-wrapper {
    padding: 1.5rem;
  }
}
</style>
