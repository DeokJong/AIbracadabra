<template>
  <section class="hero">
    <!-- 정적 JPG 배경 -->
    <!-- <img src="/hero-image.jpg" alt="여행지 전경" class="hero-bg" /> -->

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
// 특별한 로직은 없고, 라우터 이동만 처리
</script>

<style scoped>
.hero {
  position: relative;
  width: 100%;
  height: 70vh;         /* 화면 높이의 70% */
  overflow: hidden;
}
.hero-bg {
  position: absolute;
  top: 50%; left: 50%;
  width: auto; height: 100%;
  min-width: 100%; min-height: 100%;
  transform: translate(-50%, -50%);
  object-fit: cover;
  z-index: 1;
}

/* 반투명 그라데이션 오버레이 */
.hero::after {
  content: "";
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
