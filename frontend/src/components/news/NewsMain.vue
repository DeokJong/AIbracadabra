<template>
  <div class="news-container">
    <h2 class="news-title">최신 뉴스</h2>
    <div class="news-list">
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
.news-container {
  background: #f8f9fa;
  padding: 2rem;
  border-radius: 12px;
}

.news-title {
  font-size: 1.8rem;
  font-weight: bold;
  text-align: center;
  margin-bottom: 1.5rem;
}

.news-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
}
</style>
