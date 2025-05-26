<template>
  <v-app-bar flat height="70" class="modern-header" elevate-on-scroll>
    <v-container
      fluid
      class="px-4"
      :class="{ 'narrow-container': isNarrow }"
    >
      <v-row align="center" no-gutters>
        <!-- 로고 -->
        <v-col cols="auto">
          <router-link to="/" class="logo-container">
            <img src="@/assets/logo2.png" alt="로고" class="logo" />
            <span class="logo-text">TourGuide</span>
          </router-link>
        </v-col>

        <v-spacer />

        <!-- 네비게이션 버튼들 -->
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

.logo-container {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  text-decoration: none;
  color: white;
}

.logo {
  height: 45px;
  filter: brightness(0) invert(1);
}

.logo-text {
  font-size: 1.3rem;
  font-weight: 700;
  letter-spacing: -0.5px;
}

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
}
</style>
