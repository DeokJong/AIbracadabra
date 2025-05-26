<template>
  <div class="home">
    <HeroSection />
    <HomeTabs /> <!-- 탭 컴포넌트 -->
    <NewsMain /> <!-- 뉴스 컴포넌트 -->

  </div>
</template>

<script setup>
import HeroSection from '@/components/common/HeroSection.vue'
import HomeTabs from '@/components/board/HomeTab.vue'
import NewsMain from '@/components/news/NewsMain.vue'
</script>

<style scoped>
.home {
  background: aliceblue;
  display: flex;
  flex-direction: column;
  gap: 3rem;
  padding: 2rem;
}
</style>
