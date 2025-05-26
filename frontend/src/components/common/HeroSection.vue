<template>
  <section class="modern-hero">
    <!-- 배경 슬라이드 -->
    <div class="hero-background">
      <img
        v-for="(src, idx) in images"
        :key="idx"
        :src="src"
        class="hero-bg"
        :class="{ active: idx === currentIndex }"
        alt="여행 배경"
      />
      <div class="hero-gradient"></div>
    </div>

    <!-- 메인 콘텐츠 -->
    <div class="hero-container">
      <div class="hero-content">
        <!-- 좌측 메인 콘텐츠 -->
        <div class="hero-main">
          <div class="hero-badge">
            <span class="badge-icon">🤖</span>
            <span class="badge-text">AI 여행 어시스턴트</span>
          </div>
          
          <h1 class="hero-title">
            <span class="title-line-1">나만의 AI 여행 비서와</span>
            <span class="title-line-2 gradient-text">함께 여행을 시작하세요</span>
          </h1>
          
          <p class="hero-description">
            전국 관광지 정보부터 맞춤형 여행 코스까지,<br>
            스마트한 여행의 모든 것을 경험해보세요
          </p>

          <!-- 검색 바 -->
          <div class="hero-search">
            <div class="search-container">
              <div class="search-input-group">
                <span class="search-icon">🔍</span>
                <input 
                  v-model="searchQuery"
                  type="text" 
                  placeholder="어디로 여행을 떠나고 싶으신가요?"
                  class="search-input"
                  @keyup.enter="handleSearch"
                />
              </div>
              <button class="search-btn" @click="handleSearch">
                <span class="btn-text">검색하기</span>
                <span class="btn-arrow">→</span>
              </button>
            </div>
          </div>

          <!-- 액션 버튼들 -->
          <div class="hero-actions">
            <button class="action-btn primary" @click="$router.push('/map')">
              <span class="btn-icon">🗺️</span>
              <div class="btn-content">
                <span class="btn-title">지도에서 찾기</span>
                <span class="btn-subtitle">실시간 관광지 정보</span>
              </div>
            </button>
            
            <button class="action-btn secondary" @click="$router.push('/board')">
              <span class="btn-icon">💬</span>
              <div class="btn-content">
                <span class="btn-title">여행 후기</span>
                <span class="btn-subtitle">생생한 여행 이야기</span>
              </div>
            </button>
          </div>

          <!-- 통계 정보 -->
          <div class="hero-stats">
            <div class="stat-item">
              <span class="stat-number">1,200+</span>
              <span class="stat-label">관광지</span>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <span class="stat-number">50K+</span>
              <span class="stat-label">여행객</span>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <span class="stat-number">15K+</span>
              <span class="stat-label">후기</span>
            </div>
          </div>
        </div>

        <!-- 우측 카드들 -->
        <div class="hero-sidebar">
          <!-- 인기 여행지 카드 -->
          <div class="feature-card popular-destinations">
            <div class="card-header">
              <h3 class="card-title">
                <span class="card-icon">🔥</span>
                인기 여행지
              </h3>
            </div>
            <div class="destinations-list">
              <div class="destination-item" v-for="dest in popularDestinations" :key="dest.id">
                <img :src="dest.image" :alt="dest.name" class="dest-image" />
                <div class="dest-info">
                  <span class="dest-name">{{ dest.name }}</span>
                  <span class="dest-visitors">{{ dest.visitors }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 실시간 후기 카드 -->
          <div class="feature-card live-reviews">
            <div class="card-header">
              <h3 class="card-title">
                <span class="card-icon">⭐</span>
                실시간 후기
              </h3>
            </div>
            <div class="review-item">
              <div class="review-avatar">김**</div>
              <div class="review-content">
                <p class="review-text">"AI 추천으로 찾은 숨은 명소가 정말 좋았어요!"</p>
                <div class="review-rating">★★★★★</div>
              </div>
            </div>
          </div>

          <!-- 날씨 정보 카드 -->
          <div class="feature-card weather-card">
            <div class="card-header">
              <h3 class="card-title">
                <span class="card-icon">🌤️</span>
                오늘의 날씨
              </h3>
            </div>
            <div class="weather-info">
              <div class="weather-temp">23°C</div>
              <div class="weather-desc">맑음 · 여행하기 좋은 날</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 하단 스크롤 인디케이터 -->
      <div class="scroll-indicator">
        <div class="scroll-text">더 많은 정보 보기</div>
        <div class="scroll-arrow">↓</div>
      </div>
    </div>

    <!-- 배경 파티클 효과 -->
    <div class="hero-particles">
      <div v-for="i in 20" :key="i" class="particle" :style="getParticleStyle(i)"></div>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useHeroCarousel } from '@/hooks/useHeroCarousel'

const router = useRouter()
const { images, currentIndex } = useHeroCarousel(4000)

const searchQuery = ref('')

// 더미 데이터
const popularDestinations = ref([
  { id: 1, name: '제주도', visitors: '1.2M', image: '/images/jeju.jpg' },
  { id: 2, name: '부산', visitors: '890K', image: '/images/busan.jpg' },
  { id: 3, name: '경주', visitors: '650K', image: '/images/gyeongju.jpg' },
])

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    router.push(`/map?search=${encodeURIComponent(searchQuery.value)}`)
  }
}

const getParticleStyle = (index) => {
  return {
    left: `${Math.random() * 100}%`,
    top: `${Math.random() * 100}%`,
    animationDelay: `${Math.random() * 10}s`,
    animationDuration: `${8 + Math.random() * 4}s`
  }
}
</script>

<style scoped>
.modern-hero {
  position: relative;
  width: 100%;
  height: 100vh;
  min-height: 800px;
  overflow: hidden;
  background: #0a0a0a;
}

/* 배경 이미지 */
.hero-background {
  position: absolute;
  inset: 0;
  z-index: 1;
}

.hero-bg {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  opacity: 0;
  transition: opacity 2s ease-in-out;
  filter: brightness(0.7);
}

.hero-bg.active {
  opacity: 1;
}

.hero-gradient {
  position: absolute;
  inset: 0;
  background: linear-gradient(
    135deg,
    rgba(75, 109, 255, 0.8) 0%,
    rgba(109, 199, 252, 0.6) 50%,
    rgba(0, 0, 0, 0.7) 100%
  );
  z-index: 2;
}

/* 메인 컨테이너 */
.hero-container {
  position: relative;
  z-index: 3;
  height: 100%;
  max-width: 1400px;
  margin: 0 auto;
  padding: 2rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.hero-content {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 4rem;
  align-items: center;
}

/* 좌측 메인 콘텐츠 */
.hero-main {
  color: white;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 50px;
  padding: 0.75rem 1.5rem;
  margin-bottom: 2rem;
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-10px); }
}

.badge-icon {
  font-size: 1.2rem;
}

.badge-text {
  font-weight: 600;
  font-size: 0.9rem;
}

.hero-title {
  font-size: 3.5rem;
  font-weight: 800;
  line-height: 1.2;
  margin-bottom: 1.5rem;
}

.title-line-1 {
  display: block;
  animation: slideInLeft 1s ease-out;
}

.title-line-2 {
  display: block;
  animation: slideInRight 1s ease-out 0.3s both;
}

.gradient-text {
  background: linear-gradient(135deg, #FFD700, #FFA500, #FF6B6B);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-size: 200% 200%;
  animation: gradientShift 3s ease infinite;
}

@keyframes gradientShift {
  0%, 100% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
}

@keyframes slideInLeft {
  from { opacity: 0; transform: translateX(-50px); }
  to { opacity: 1; transform: translateX(0); }
}

@keyframes slideInRight {
  from { opacity: 0; transform: translateX(50px); }
  to { opacity: 1; transform: translateX(0); }
}

.hero-description {
  font-size: 1.2rem;
  line-height: 1.6;
  margin-bottom: 3rem;
  opacity: 0.9;
}

/* 검색 바 */
.hero-search {
  margin-bottom: 3rem;
  animation: slideInUp 1s ease-out 0.6s both;
}

.search-container {
  display: flex;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 60px;
  padding: 0.5rem;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
}

.search-container:hover {
  transform: translateY(-2px);
  box-shadow: 0 15px 50px rgba(0, 0, 0, 0.3);
}

.search-input-group {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 0 1.5rem;
}

.search-icon {
  font-size: 1.2rem;
  color: #666;
}

.search-input {
  flex: 1;
  border: none;
  background: transparent;
  font-size: 1.1rem;
  color: #333;
  outline: none;
}

.search-input::placeholder {
  color: #999;
}

.search-btn {
  background: linear-gradient(135deg, #4B6DFF, #6DC7FC);
  border: none;
  border-radius: 50px;
  padding: 1rem 2rem;
  color: white;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transition: all 0.3s ease;
  box-shadow: 0 5px 20px rgba(75, 109, 255, 0.4);
}

.search-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 30px rgba(75, 109, 255, 0.6);
}

.btn-arrow {
  transition: transform 0.3s ease;
}

.search-btn:hover .btn-arrow {
  transform: translateX(5px);
}

@keyframes slideInUp {
  from { opacity: 0; transform: translateY(30px); }
  to { opacity: 1; transform: translateY(0); }
}

/* 액션 버튼들 */
.hero-actions {
  display: flex;
  gap: 1.5rem;
  margin-bottom: 3rem;
  animation: slideInUp 1s ease-out 0.9s both;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1.25rem 2rem;
  border: none;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
  color: white;
  text-align: left;
}

.action-btn.primary {
  background: rgba(255, 255, 255, 0.15);
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.action-btn.secondary {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.action-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
}

.action-btn.primary:hover {
  background: rgba(255, 255, 255, 0.25);
}

.action-btn.secondary:hover {
  background: rgba(255, 255, 255, 0.2);
}

.btn-icon {
  font-size: 1.5rem;
}

.btn-content {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.btn-title {
  font-weight: 600;
  font-size: 1rem;
}

.btn-subtitle {
  font-size: 0.8rem;
  opacity: 0.8;
}

/* 통계 정보 */
.hero-stats {
  display: flex;
  align-items: center;
  gap: 2rem;
  animation: slideInUp 1s ease-out 1.2s both;
}

.stat-item {
  text-align: center;
}

.stat-number {
  display: block;
  font-size: 2rem;
  font-weight: 700;
  color: #FFD700;
  margin-bottom: 0.25rem;
}

.stat-label {
  font-size: 0.9rem;
  opacity: 0.8;
}

.stat-divider {
  width: 1px;
  height: 40px;
  background: rgba(255, 255, 255, 0.3);
}

/* 우측 사이드바 */
.hero-sidebar {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  animation: slideInRight 1s ease-out 0.8s both;
}

.feature-card {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(15px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  padding: 1.5rem;
  color: white;
  transition: all 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
  background: rgba(255, 255, 255, 0.15);
}

.card-header {
  margin-bottom: 1rem;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1.1rem;
  font-weight: 600;
  margin: 0;
}

.card-icon {
  font-size: 1.2rem;
}

/* 인기 여행지 */
.destinations-list {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.destination-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 0.5rem;
  border-radius: 12px;
  transition: background 0.3s ease;
}

.destination-item:hover {
  background: rgba(255, 255, 255, 0.1);
}

.dest-image {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  object-fit: cover;
}

.dest-info {
  flex: 1;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dest-name {
  font-weight: 500;
}

.dest-visitors {
  font-size: 0.8rem;
  opacity: 0.7;
}

/* 실시간 후기 */
.review-item {
  display: flex;
  gap: 1rem;
  align-items: flex-start;
}

.review-avatar {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #FFD700, #FFA500);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #333;
  font-weight: 600;
  font-size: 0.9rem;
}

.review-content {
  flex: 1;
}

.review-text {
  font-size: 0.9rem;
  line-height: 1.4;
  margin-bottom: 0.5rem;
}

.review-rating {
  color: #FFD700;
  font-size: 0.9rem;
}

/* 날씨 정보 */
.weather-info {
  text-align: center;
}

.weather-temp {
  font-size: 2rem;
  font-weight: 700;
  color: #FFD700;
  margin-bottom: 0.5rem;
}

.weather-desc {
  font-size: 0.9rem;
  opacity: 0.8;
}

/* 스크롤 인디케이터 */
.scroll-indicator {
  position: absolute;
  bottom: 2rem;
  left: 50%;
  transform: translateX(-50%);
  text-align: center;
  color: white;
  animation: bounce 2s infinite;
}

.scroll-text {
  font-size: 0.9rem;
  margin-bottom: 0.5rem;
  opacity: 0.8;
}

.scroll-arrow {
  font-size: 1.5rem;
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% { transform: translateX(-50%) translateY(0); }
  40% { transform: translateX(-50%) translateY(-10px); }
  60% { transform: translateX(-50%) translateY(-5px); }
}

/* 파티클 효과 */
.hero-particles {
  position: absolute;
  inset: 0;
  pointer-events: none;
  z-index: 2;
}

.particle {
  position: absolute;
  width: 4px;
  height: 4px;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 50%;
  animation: float-particle linear infinite;
}

@keyframes float-particle {
  0% {
    transform: translateY(100vh) scale(0);
    opacity: 0;
  }
  10% {
    opacity: 1;
  }
  90% {
    opacity: 1;
  }
  100% {
    transform: translateY(-100vh) scale(1);
    opacity: 0;
  }
}

/* 반응형 디자인 */
@media (max-width: 1200px) {
  .hero-content {
    grid-template-columns: 1fr;
    gap: 3rem;
  }
  
  .hero-sidebar {
    flex-direction: row;
    overflow-x: auto;
  }
  
  .feature-card {
    min-width: 250px;
  }
}

@media (max-width: 768px) {
  .modern-hero {
    min-height: 100vh;
  }
  
  .hero-container {
    padding: 1rem;
  }
  
  .hero-title {
    font-size: 2.5rem;
  }
  
  .hero-actions {
    flex-direction: column;
  }
  
  .action-btn {
    justify-content: center;
    text-align: center;
  }
  
  .hero-stats {
    justify-content: center;
    flex-wrap: wrap;
  }
  
  .search-container {
    flex-direction: column;
    gap: 1rem;
    padding: 1rem;
  }
  
  .search-btn {
    align-self: stretch;
    justify-content: center;
  }
  
  .hero-sidebar {
    flex-direction: column;
  }
  
  .feature-card {
    min-width: auto;
  }
}

@media (max-width: 480px) {
  .hero-title {
    font-size: 2rem;
  }
  
  .hero-description {
    font-size: 1rem;
  }
  
  .hero-stats {
    gap: 1rem;
  }
  
  .stat-number {
    font-size: 1.5rem;
  }
}
</style>
