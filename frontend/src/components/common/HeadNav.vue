<template>
  <v-app-bar flat height="70" class="modern-header" elevate-on-scroll>
    <v-container
      fluid
      class="px-4"
      :class="{ 'narrow-container': isNarrow }"
    >
      <v-row align="center" no-gutters>
        <!-- 새로운 SVG 로고 -->
        <v-col cols="auto">
          <router-link to="/" class="logo-container">
            <!-- 인라인 SVG 로고 -->
            <div class="logo-svg-wrapper">
              <svg width="45" height="45" viewBox="0 0 100 100" class="tour-logo">
                <!-- 배경 원 -->
                <circle cx="50" cy="50" r="48" fill="url(#primaryGradient)" stroke="rgba(255,255,255,0.2)" stroke-width="2"/>
                
                <!-- 나침반 외곽 링 -->
                <circle cx="50" cy="50" r="35" fill="none" stroke="rgba(255,255,255,0.4)" stroke-width="2"/>
                
                <!-- 나침반 방향 표시 -->
                <g transform="translate(50,50)">
                  <!-- 북쪽 화살표 (메인) -->
                  <path d="M0,-30 L6,-24 L2,-20 L0,-22 L-2,-20 L-6,-24 Z" fill="white" opacity="0.95"/>
                  <!-- 동쪽 표시 -->
                  <circle cx="25" cy="0" r="2" fill="rgba(255,255,255,0.7)"/>
                  <!-- 남쪽 표시 -->
                  <circle cx="0" cy="25" r="2" fill="rgba(255,255,255,0.7)"/>
                  <!-- 서쪽 표시 -->
                  <circle cx="-25" cy="0" r="2" fill="rgba(255,255,255,0.7)"/>
                </g>
                
                <!-- 중앙 지구본 -->
                <circle cx="50" cy="50" r="16" fill="rgba(255,255,255,0.95)"/>
                <circle cx="50" cy="50" r="14" fill="url(#earthGradient)"/>
                
                <!-- 지구본 경위도선 -->
                <g transform="translate(50,50)" stroke="rgba(255,255,255,0.9)" stroke-width="1.5" fill="none">
                  <!-- 적도 -->
                  <line x1="-14" y1="0" x2="14" y2="0"/>
                  <!-- 본초자오선 -->
                  <line x1="0" y1="-14" x2="0" y2="14"/>
                  <!-- 타원형 경도선들 -->
                  <ellipse rx="14" ry="7" opacity="0.7"/>
                  <ellipse rx="7" ry="14" opacity="0.7"/>
                </g>
                
                <!-- 중앙 포인트 -->
                <circle cx="50" cy="50" r="3" fill="white" opacity="0.9"/>
                
                <!-- 그라디언트 정의 -->
                <defs>
                  <linearGradient id="primaryGradient" x1="0%" y1="0%" x2="100%" y2="100%">
                    <stop offset="0%" style="stop-color:#667eea;stop-opacity:1" />
                    <stop offset="50%" style="stop-color:#764ba2;stop-opacity:1" />
                    <stop offset="100%" style="stop-color:#667eea;stop-opacity:1" />
                  </linearGradient>
                  <radialGradient id="earthGradient" cx="30%" cy="30%">
                    <stop offset="0%" style="stop-color:#4facfe;stop-opacity:1" />
                    <stop offset="70%" style="stop-color:#00f2fe;stop-opacity:1" />
                    <stop offset="100%" style="stop-color:#667eea;stop-opacity:0.8" />
                  </radialGradient>
                </defs>
              </svg>
            </div>
            <span class="logo-text">AIbracadabra</span>
          </router-link>
        </v-col>

        <v-spacer />

        <!-- 기존 네비게이션 코드 그대로 유지... -->
        <v-col cols="auto" class="d-none d-md-flex align-center">
          <nav class="nav-menu">
            <router-link to="/map" class="nav-item">
              <span class="nav-icon">🗺️</span>
              관광지도
            </router-link>
            <router-link to="/board" class="nav-item">
              <span class="nav-icon">📝</span>
              게시판
            </router-link>
            <router-link to="/notice" class="nav-item">
              <span class="nav-icon">📢</span>
              공지사항
            </router-link>
            <router-link to="/qna" class="nav-item">
              <span class="nav-icon">💬</span>
              문의하기
            </router-link>
            <router-link to="/news" class="nav-item">
              <span class="nav-icon">📰</span>
              뉴스정보
            </router-link>
          </nav>
          
          <div class="auth-buttons">
            <template v-if="!isLoggined">
              <router-link to="/login" class="auth-btn login-btn">로그인</router-link>
              <router-link to="/signup" class="auth-btn signup-btn">회원가입</router-link>
            </template>
            <template v-else>
              <router-link to="/mypage" class="user-profile">
                <div class="user-avatar">{{ userInfo?.name?.charAt(0) || 'U' }}</div>
                <span class="user-name">{{ userInfo?.name }}</span>
              </router-link>
              <button @click="logout" class="auth-btn logout-btn">로그아웃</button>
            </template>
          </div>
        </v-col>

        <!-- 모바일 메뉴 버튼 -->
        <v-col cols="auto" class="d-md-none">
          <v-btn icon @click="mobileMenuOpen = !mobileMenuOpen">
            <v-icon>mdi-menu</v-icon>
          </v-btn>
        </v-col>
      </v-row>
    </v-container>

    <!-- 모바일 메뉴 -->
    <div v-if="mobileMenuOpen" class="mobile-menu">
      <div class="mobile-nav">
        <router-link to="/map" class="mobile-nav-item" @click="mobileMenuOpen = false">
          🗺️ 관광지도
        </router-link>
        <router-link to="/board" class="mobile-nav-item" @click="mobileMenuOpen = false">
          📝 게시판
        </router-link>
        <router-link to="/notice" class="mobile-nav-item" @click="mobileMenuOpen = false">
          📢 공지사항
        </router-link>
        <router-link to="/qna" class="mobile-nav-item" @click="mobileMenuOpen = false">
          💬 문의하기
        </router-link>
        <router-link to="/news" class="mobile-nav-item" @click="mobileMenuOpen = false">
          📰 뉴스정보
        </router-link>
      </div>
      
      <div class="mobile-auth">
        <template v-if="!isLoggined">
          <router-link to="/login" class="mobile-auth-btn" @click="mobileMenuOpen = false">
            로그인
          </router-link>
          <router-link to="/signup" class="mobile-auth-btn" @click="mobileMenuOpen = false">
            회원가입
          </router-link>
        </template>
        <template v-else>
          <router-link to="/mypage" class="mobile-auth-btn" @click="mobileMenuOpen = false">
            {{ userInfo?.name }}님
          </router-link>
          <button @click="logout; mobileMenuOpen = false" class="mobile-auth-btn">
            로그아웃
          </button>
        </template>
      </div>
    </div>
  </v-app-bar>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import { useAuth } from '@/hooks/useAuth'
import { storeToRefs } from 'pinia'

const { isLoggined, userInfo } = storeToRefs(useAuth())
const { logout } = useAuth()

const route = useRoute()
const mobileMenuOpen = ref(false)

const isNarrow = computed(() => {
  const fullWidthPages = ['/map', '/login', '/signup']
  return !fullWidthPages.includes(route.path)
})

const maxWidthValue = computed(() => {
  const fullWidthPages = ['/map', '/login', '/signup']
  return fullWidthPages.includes(route.path) ? 'none' : '1200px'
})
</script>

<style scoped>
.modern-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  max-width: v-bind(maxWidthValue);
  margin: 0 auto;
  left: auto !important;
  right: auto !important;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

/* 새로운 로고 스타일 */
.logo-container {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  text-decoration: none;
  color: white;
  transition: all 0.3s ease;
}

.logo-container:hover {
  transform: translateY(-2px);
}

.logo-svg-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
}

.tour-logo {
  filter: drop-shadow(0 2px 8px rgba(0, 0, 0, 0.3));
  transition: all 0.4s ease;
}

.logo-container:hover .tour-logo {
  transform: rotate(10deg) scale(1.05);
  filter: drop-shadow(0 4px 15px rgba(0, 0, 0, 0.4));
}

.logo-text {
  font-size: 1.3rem;
  font-weight: 700;
  letter-spacing: -0.5px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
  transition: all 0.3s ease;
}

.logo-container:hover .logo-text {
  text-shadow: 0 3px 8px rgba(0, 0, 0, 0.4);
}

/* 기존 스타일들 유지 */
.nav-menu {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-right: 2rem;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  padding: 0.6rem 1rem;
  color: rgba(255, 255, 255, 0.9);
  text-decoration: none;
  border-radius: 25px;
  font-weight: 500;
  font-size: 0.9rem;
  transition: all 0.3s ease;
  position: relative;
}

.nav-item:hover {
  background: rgba(255, 255, 255, 0.15);
  color: white;
  transform: translateY(-1px);
}

.nav-item.router-link-active {
  background: rgba(255, 255, 255, 0.2);
  color: white;
}

.nav-icon {
  font-size: 1rem;
}

.auth-buttons {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.auth-btn {
  padding: 0.6rem 1.2rem;
  border-radius: 25px;
  font-weight: 500;
  font-size: 0.9rem;
  text-decoration: none;
  transition: all 0.3s ease;
  border: none;
  cursor: pointer;
}

.login-btn {
  color: white;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.login-btn:hover {
  background: rgba(255, 255, 255, 0.2);
}

.signup-btn {
  background: white;
  color: #667eea;
}

.signup-btn:hover {
  background: #f8f9fa;
  transform: translateY(-1px);
}

.logout-btn {
  color: white;
  background: rgba(255, 255, 255, 0.1);
}

.logout-btn:hover {
  background: rgba(255, 255, 255, 0.2);
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: white;
  text-decoration: none;
  padding: 0.4rem 1rem;
  border-radius: 25px;
  background: rgba(255, 255, 255, 0.1);
  transition: all 0.3s ease;
}

.user-profile:hover {
  background: rgba(255, 255, 255, 0.2);
}

.user-avatar {
  width: 32px;
  height: 32px;
  background: white;
  color: #667eea;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 0.9rem;
}

.user-name {
  font-weight: 500;
}

/* 모바일 메뉴 */
.mobile-menu {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: white;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  border-radius: 0 0 16px 16px;
}

.mobile-nav {
  padding: 1rem;
  border-bottom: 1px solid #f0f0f0;
}

.mobile-nav-item {
  display: block;
  padding: 1rem;
  color: #333;
  text-decoration: none;
  border-radius: 8px;
  margin-bottom: 0.5rem;
  transition: all 0.2s ease;
}

.mobile-nav-item:hover {
  background: #f8f9fa;
  color: #667eea;
}

.mobile-auth {
  padding: 1rem;
  display: flex;
  gap: 0.5rem;
}

.mobile-auth-btn {
  flex: 1;
  padding: 0.75rem;
  text-align: center;
  background: #667eea;
  color: white;
  text-decoration: none;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease;
}

.mobile-auth-btn:hover {
  background: #5a67d8;
}

.narrow-container {
  width: 100%;
  margin: 0 auto;
  display: flex;
  align-items: center;
}

@media (max-width: 768px) {
  .logo-text {
    display: none;
  }
  
  .tour-logo {
    width: 35px;
    height: 35px;
  }
}
</style>
