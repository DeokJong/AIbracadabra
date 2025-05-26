<template>
  <section class="hero">
    <img
      v-for="(src, idx) in images"
      :key="idx"
      :src="src"
      class="hero-bg"
      :class="{ active: idx === currentIndex }"
      alt="여행 배경"
    />


    <div class="hero-overlay">
      <h1 class="hero-title">나만의 AI 여행 비서와 함께 여행을 시작하세요</h1>
      <p class="hero-subtitle">매달 추천 축제 · 핫스팟 · 인기 게시글</p>
      <button class="hero-cta" @click="$router.push('/map')">
        여행지 검색 ▶
      </button>
    </div>
  </section>
</template>

<script setup>
import { useHeroCarousel } from '@/hooks/useHeroCarousel'

// interval만 넘기거나, 아무 것도 안 넘겨도 OK
const { images, currentIndex } = useHeroCarousel(3000)
</script>


<style scoped>
.hero {
  position: relative;
  width: 100%;
  height: 70vh;
  overflow: hidden;
}
.hero-bg {
  position: absolute;
  top: 50%;
  left: 50%;
  min-width: 100%;
  min-height: 100%;
  transform: translate(-50%, -50%);
  object-fit: cover;
  opacity: 0;
  transition: opacity 1.5s ease-in-out;
  z-index: 1;
}
.hero-bg.active {
  opacity: 1;
}
.hero::after {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(
    to bottom,
    rgba(0, 0, 0, 0.4),
    rgba(0, 0, 0, 0.6)
  );
  z-index: 2;
}
.hero-overlay {
  position: relative;
  z-index: 3;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #fff;
  text-align: center;
  padding: 0 1rem;
}
.hero-title {
  font-size: 2.5rem;
  font-weight: bold;
  margin-bottom: 0.5rem;
}
.hero-subtitle {
  font-size: 1.2rem;
  margin-bottom: 1.5rem;
}
.hero-cta {
  padding: 0.8rem 1.6rem;
  font-size: 1rem;
  background: #ff6347;
  border: none;
  border-radius: 4px;
  color: #fff;
  cursor: pointer;
  transition: background 0.2s;
}
.hero-cta:hover {
  background: #e5533d;
}
</style>