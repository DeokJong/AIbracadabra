<template>
  <div>모든 지역 뉴스보기</div>
  <div class="home">
    <RegionNews
      v-for="r in regions"
      :key="r.code"
      :regionName="r.name"
      :sidoCode="r.code"
    />
  </div>
</template>

<script setup lang="ts">
import RegionNews from '@/components/news/RegionNews.vue'

const regions = [
  { name: '서울', code: 4 },
  { name: '인청/경기', code: 5 },
  { name: '대전/충청', code: 6 },
  { name: '전라북도', code: 7 },
  { name: '광주/전남', code: 8 },
  { name: '대구/경북', code: 9 },
  { name: '부산/울산/경남', code: 10 },
  { name: '강원', code: 11 },
  { name: '제주', code: 12 }
]
</script>

<style scoped>
.home {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1rem;
  max-width: 1024px;
  margin: auto;
  padding: 1rem;
}
</style>
