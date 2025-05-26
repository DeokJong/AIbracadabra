<template>
  <div class="max-w-5xl mx-auto py-8 px-4 space-y-12">

    <!-- 옵션 1: 탭 레이아웃 -->
    <section class="bg-white rounded-2xl shadow-lg">
      <div class="border-b mypage">
        <button
          v-for="tab in tabs"
          :key="tab"
          @click="activeTab = tab"
          :class="['px-6 py-3 font-medium', activeTab === tab ? 'border-b-2 border-indigo-500 text-indigo-600' : 'text-gray-600']"
        >
          {{ tabLabels[tab] }}
        </button>
      </div>
      <div class="p-6" >
        <div v-if="activeTab === 'profile'"><MemberModify /></div>
        <div v-else-if="activeTab === 'boards'"><MyBoards /></div>
        <div v-else><MyComments /></div>
      </div>
    </section>

    <!-- 옵션 2: 사이드바 네비게이션 -->
    <!-- <section class="flex bg-white rounded-2xl shadow-lg overflow-hidden">
      <nav class="w-1/4 bg-indigo-50 p-6 space-y-4">
        <button
          v-for="nav in navItems"
          :key="nav"
          @click="selectedNav = nav"
          :class="['block w-full text-left px-4 py-2 rounded-lg', selectedNav === nav ? 'bg-indigo-500 text-white' : 'text-gray-700 hover:bg-indigo-100']"
        >
          {{ navLabels[nav] }}
        </button>
      </nav>
      <div class="w-3/4 p-6">
        <div v-if="selectedNav === 'profile'"><MemberModify /></div>
        <div v-else-if="selectedNav === 'boards'"><MyBoards /></div>
        <div v-else><MyComments /></div>
      </div>
    </section> -->

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
</script>

<style scoped>
/* 추가 커스텀 스타일이 필요하면 여기에 작성하세요 */

.mypage :hover{
    color: #83bcea;
} 

</style>
