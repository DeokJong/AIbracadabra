<template>
  <div class="modern-home">
    <!-- Hero Section -->
    <section class="hero-wrapper">
      <HeroSection />
    </section>

    <!-- Main Content Area -->
    <main class="main-content">
      <!-- Community Highlight Section -->
      <section class="community-section">
        <div class="section-container">
          <div class="section-intro">
            <h2 class="section-title">
              <span class="title-accent">🔥</span>
              커뮤니티 하이라이트
            </h2>
            <p class="section-description">
              실시간으로 업데이트되는 인기 게시글과 최신 소식을 확인하세요
            </p>
          </div>
          <div class="tabs-container">
            <HomeTabs />
          </div>
        </div>
      </section>

      <!-- Divider Section -->
      <section class="divider-section">
        <div class="section-container">
          <div class="divider-content">
            <div class="divider-stats">
              <div class="stat-card">
                <div class="stat-icon">🗺️</div>
                <div class="stat-info">
                  <span class="stat-number">1,200+</span>
                  <span class="stat-label">관광지</span>
                </div>
              </div>
              <div class="stat-card">
                <div class="stat-icon">👥</div>
                <div class="stat-info">
                  <span class="stat-number">50K+</span>
                  <span class="stat-label">여행객</span>
                </div>
              </div>
              <div class="stat-card">
                <div class="stat-icon">⭐</div>
                <div class="stat-info">
                  <span class="stat-number">15K+</span>
                  <span class="stat-label">후기</span>
                </div>
              </div>
            </div>
            <div class="divider-cta">
              <h3 class="cta-title">지금 바로 여행을 계획해보세요!</h3>
              <router-link to="/map" class="cta-button">
                <span class="cta-icon">🚀</span>
                여행 계획하기
              </router-link>
            </div>
          </div>
        </div>
      </section>

      <!-- News Section -->
      <section class="news-section">
        <div class="section-container">
          <div class="section-intro">
            <h2 class="section-title">
              <span class="title-accent">📰</span>
              지역별 최신 뉴스
            </h2>
            <p class="section-description">
              전국 각 지역의 생생한 소식을 실시간으로 만나보세요
            </p>
          </div>
          <div class="news-container">
            <NewsMain />
          </div>
        </div>
      </section>

      <!-- Additional Features Section -->
      <section class="features-section">
        <div class="section-container">
          <div class="features-grid">
            <div class="feature-card large">
              <div class="feature-content">
                <div class="feature-icon">🤖</div>
                <h3 class="feature-title">AI 여행 추천</h3>
                <p class="feature-description">
                  인공지능이 분석한 맞춤형 여행지를 추천받아보세요
                </p>
                <router-link to="/map" class="feature-link">
                  체험해보기 →
                </router-link>
              </div>
              <div class="feature-visual">
                <div class="visual-placeholder">AI 추천 시스템</div>
              </div>
            </div>

            <div class="feature-card">
              <div class="feature-content">
                <div class="feature-icon">📱</div>
                <h3 class="feature-title">모바일 최적화</h3>
                <p class="feature-description">
                  언제 어디서나 편리하게 여행 정보를 확인하세요
                </p>
              </div>
            </div>

            <div class="feature-card">
              <div class="feature-content">
                <div class="feature-icon">🌍</div>
                <h3 class="feature-title">실시간 정보</h3>
                <p class="feature-description">
                  최신 관광지 정보와 날씨를 실시간으로 제공합니다
                </p>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
import HeroSection from '@/components/common/HeroSection.vue'
import HomeTabs from '@/components/board/HomeTab.vue'
import NewsMain from '@/components/news/NewsMain.vue'
</script>

<style scoped>
/* 메인 컨테이너 */
.modern-home {
  background: #ffffff;
  min-height: 100vh;
}

/* Hero Section */
.hero-wrapper {
  position: relative;
  z-index: 1;
}

/* Main Content */
.main-content {
  position: relative;
  z-index: 2;
}

/* 공통 섹션 컨테이너 */
.section-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 2rem;
}

/* 섹션 공통 스타일 */
.section-intro {
  text-align: center;
  margin-bottom: 4rem;
  animation: fadeInUp 0.8s ease-out;
}

.section-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  font-size: 2.5rem;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 1rem;
}

.title-accent {
  font-size: 2.8rem;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
}

.section-description {
  font-size: 1.2rem;
  color: #6c757d;
  max-width: 600px;
  margin: 0 auto;
  line-height: 1.6;
}

/* Community Section */
.community-section {
  padding: 6rem 0;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  position: relative;
  overflow: hidden;
}

.community-section::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(102, 126, 234, 0.05) 0%, transparent 70%);
  animation: float 20s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translate(0, 0) rotate(0deg); }
  33% { transform: translate(30px, -30px) rotate(120deg); }
  66% { transform: translate(-20px, 20px) rotate(240deg); }
}

.tabs-container {
  position: relative;
  z-index: 2;
  animation: slideInUp 0.8s ease-out 0.2s both;
}

/* Divider Section */
.divider-section {
  padding: 5rem 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  position: relative;
}

.divider-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.05'%3E%3Ccircle cx='30' cy='30' r='4'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
}

.divider-content {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 4rem;
  align-items: center;
  position: relative;
  z-index: 2;
}

.divider-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 1rem;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 16px;
  padding: 2rem;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  background: rgba(255, 255, 255, 0.15);
}

.stat-icon {
  font-size: 2rem;
  width: 60px;
  height: 60px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-number {
  font-size: 2rem;
  font-weight: 700;
  color: #FFD700;
  margin-bottom: 0.25rem;
}

.stat-label {
  font-size: 1rem;
  opacity: 0.9;
}

.divider-cta {
  text-align: center;
}

.cta-title {
  font-size: 1.8rem;
  font-weight: 600;
  margin-bottom: 2rem;
}

.cta-button {
  display: inline-flex;
  align-items: center;
  gap: 0.75rem;
  background: rgba(255, 255, 255, 0.9);
  color: #667eea;
  padding: 1rem 2rem;
  border-radius: 50px;
  text-decoration: none;
  font-weight: 600;
  font-size: 1.1rem;
  transition: all 0.3s ease;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.2);
}

.cta-button:hover {
  background: white;
  transform: translateY(-3px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.3);
}

.cta-icon {
  font-size: 1.3rem;
}

/* News Section */
.news-section {
  padding: 6rem 0;
  background: #ffffff;
  position: relative;
}

.news-container {
  animation: slideInUp 0.8s ease-out 0.4s both;
}

/* Features Section */
.features-section {
  padding: 6rem 0;
  background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);
}

.features-grid {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr;
  gap: 2rem;
  margin-top: 4rem;
}

.feature-card {
  background: white;
  border-radius: 20px;
  padding: 2rem;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  border: 1px solid #f0f0f0;
}

.feature-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}

.feature-card.large {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 2rem;
  align-items: center;
}

.feature-icon {
  font-size: 2.5rem;
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 1.5rem;
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

.feature-title {
  font-size: 1.4rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 1rem;
}

.feature-description {
  color: #6c757d;
  line-height: 1.6;
  margin-bottom: 1.5rem;
}

.feature-link {
  color: #667eea;
  text-decoration: none;
  font-weight: 600;
  transition: color 0.3s ease;
}

.feature-link:hover {
  color: #5a67d8;
}

.feature-visual {
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 15px;
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 600;
}

.visual-placeholder {
  text-align: center;
  opacity: 0.9;
}

/* 애니메이션 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(50px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 반응형 디자인 */
@media (max-width: 1200px) {
  .features-grid {
    grid-template-columns: 1fr;
  }
  
  .feature-card.large {
    grid-template-columns: 1fr;
    text-align: center;
  }
  
  .divider-content {
    grid-template-columns: 1fr;
    gap: 3rem;
  }
  
  .divider-stats {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .section-container {
    padding: 0 1rem;
  }
  
  .section-title {
    font-size: 2rem;
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .section-description {
    font-size: 1rem;
  }
  
  .community-section,
  .news-section,
  .features-section {
    padding: 4rem 0;
  }
  
  .divider-section {
    padding: 3rem 0;
  }
  
  .divider-stats {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  
  .stat-card {
    padding: 1.5rem;
    flex-direction: column;
    text-align: center;
  }
  
  .cta-title {
    font-size: 1.4rem;
  }
  
  .features-grid {
    gap: 1.5rem;
  }
  
  .feature-card {
    padding: 1.5rem;
  }
  
  .feature-icon {
    width: 60px;
    height: 60px;
    font-size: 2rem;
  }
}

@media (max-width: 480px) {
  .section-intro {
    margin-bottom: 2rem;
  }
  
  .cta-button {
    padding: 0.8rem 1.5rem;
    font-size: 1rem;
  }
  
  .feature-visual {
    height: 150px;
  }
}
</style>
