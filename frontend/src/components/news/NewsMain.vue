<template>
  <div class="news-main-container">
    <!-- <div class="news-header">
      <h2 class="news-title">
        <span class="title-icon">📰</span>
        지역별 최신 뉴스
      </h2>
      <p class="news-subtitle">전국 각 지역의 생생한 소식을 만나보세요</p>
    </div> -->
    
    <div class="news-grid">
      <RegionNews
        v-for="r in regions"
        :key="r.code"
        :regionName="r.name"
        :sidoCode="r.code"
      />
    </div>
  </div>
</template>

<script setup>
import RegionNews from '@/components/news/RegionNews.vue'

const regions = [
  { name: '서울', code: 4 },
  { name: '인천/경기', code: 5 },
  { name: '대전/충청', code: 6 },
  { name: '전라북도', code: 7 },
  { name: '광주/전남', code: 8 },
  { name: '대구/경북', code: 9 },
  { name: '부산/울산/경남', code: 10 },
  { name: '강원', code: 11 },
  { name: '제주', code: 12 },
]
</script>

<style scoped>
.news-main-container {
  max-width: 1200px;
  margin: 0 auto;
}

.news-header {
  text-align: center;
  margin-bottom: 3rem;
}

.news-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  font-size: 2rem;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 0.5rem;
}

.title-icon {
  font-size: 2.2rem;
}

.news-subtitle {
  color: #6c757d;
  font-size: 1.1rem;
  margin: 0;
}

.news-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 2rem;
}

@media (max-width: 768px) {
  .news-grid {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }
  
  .news-title {
    font-size: 1.6rem;
  }
  
  .news-subtitle {
    font-size: 1rem;
  }
}
</style>
