<template>
  <div class="tabs-container">
    <div class="tab-header">
      <button
        v-for="tab in tabs"
        :key="tab.name"
        :class="['tab-button', { active: activeTab === tab.name }]"
        @click="activeTab = tab.name"
      >
        {{ tab.label }}
      </button>
    </div>

    <div class="tab-content">
      <component :is="activeTabComponent" :boardType="currentBoardType" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import PopularBoard from '@/components/board/PopularBoard.vue'

const activeTab = ref('PopularBoardBoard')

const tabs = [
  { name: 'PopularBoardBoard', label: '인기 게시글' },
  { name: 'PopularBoardNotice', label: '최근 공지사항' },
  { name: 'PopularBoardQna', label: '최근 Q&A' },
]

const activeTabComponent = computed(() => PopularBoard)

const currentBoardType = computed(() => {
  if (activeTab.value === 'PopularBoardBoard') return 'board'
  if (activeTab.value === 'PopularBoardNotice') return 'notice'
  if (activeTab.value === 'PopularBoardQna') return 'qna'
})
</script>

<style scoped>
.tabs-container {
  max-width: 1200px;
  background: #f9f9f9;
  border-radius: 12px;
  padding: 2rem;
}

.tab-header {
  display: flex;
  justify-content: center;
  border-bottom: 2px solid #ddd;
  gap: 2rem;
  margin-bottom: 1rem;
}

.tab-button {
  padding: 12px 24px;
  font-weight: bold;
  color: #666;
  background: none;
  border: none;
  cursor: pointer;
  border-bottom: 3px solid transparent;
  transition: all 0.3s ease;
}

.tab-button.active {
  color: #007bff;
  border-bottom-color: #007bff;
}

.tab-button:hover {
  color: #0056b3;
}

.tab-content {
  padding-top: 2rem;
  width: 100%;
}

.tab-content > * {
  width: 100%;
}
</style>
