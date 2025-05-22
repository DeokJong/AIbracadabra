
<template>
  <div class="home">

  <HeroSection />
  <br>

    <!-- 인기 게시판만 -->
  <PopularBoard 
  title = "인기 게시글"
  boardType="board" />
  <br>

  <!-- 인기 공지사항 -->
  <PopularBoard 
  title = "인기 공지사항"
  boardType="notice" />
  <br>

  <!-- 인기 Q&A -->
  <PopularBoard
  title = "인기 Q&A"
   boardType="qna" />
   <br>
  
  <NewsMain/>
  </div>
</template>

<script setup>
import NewsMain from '@/components/news/NewsMain.vue'
import PopularBoard from '@/components/board/PopularBoard.vue'
import HeroSection from '@/components/common/HeroSection.vue'


</script>
<style scoped>
.home {
  display: flex;
  flex-direction: column;
  gap: 2rem;
  max-width: 1024px;
  margin: auto;
  padding: 1rem;
}
.popular-section {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1rem;
}
</style>