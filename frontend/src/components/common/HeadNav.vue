<template>
  <v-app-bar flat height="64" class="gradient-header" elevate-on-scroll>
    <!-- route에 따라 클래스 토글 -->
    <v-container
      fluid
      
      class="px-4"
      :class="{ 'narrow-container': isNarrow }"
    >
      <v-row align="center" no-gutters>
        <!-- 로고 -->
        <v-col cols="auto">
          <router-link to="/">
            <img src="@/assets/logo2.png" alt="로고" class="logo" />
          </router-link>
        </v-col>

        <v-spacer />

        <!-- 네비게이션 버튼들 -->
        <v-col cols="auto" class="d-flex align-center">
          <v-btn text to="/map" :isNarrow="false">관광지도</v-btn>
          <v-btn text to="/board">게시판</v-btn>
          <v-btn text to="/notice">공지사항</v-btn>
          <v-btn text to="/qna">문의하기</v-btn>
          <v-btn text to="/news">뉴스정보보기</v-btn>
          <v-btn v-if="!isLoggined" text to="/login">로그인</v-btn>
          <v-btn v-if="!isLoggined" text to="/signup">회원가입</v-btn>
          <v-btn v-if="isLoggined" text @click="logout">로그아웃</v-btn>
          <v-btn v-if="isLoggined" text to="/mypage">
            {{ userInfo.name }}
          </v-btn>
        </v-col>
      </v-row>
    </v-container>
  </v-app-bar>
</template>

<script setup lang="ts">

import { useAuth } from '@/hooks/useAuth'
import { storeToRefs } from 'pinia';

const { isLoggined, userInfo } = storeToRefs(useAuth());
const { logout } = useAuth();

const props = defineProps<{isNarrow: Boolean}> ()


</script>

<style scoped>
.gradient-header {
  /* 왼쪽 #4facfe → 오른쪽 #00f2fe 그라데이션 */
  background: linear-gradient(90deg,#4B6DFF 0%, #6DC7FC 100%);
  max-width: 1200px;
  margin: 0 auto;      
  

  left: auto !important;
  right: auto !important;
}

.logo {
  height: 40px;
  /* 필요에 따라 width:auto; */
}
.narrow-container {
  /* background: linear-gradient(90deg,#4B6DFF 0%, #6DC7FC 100%) ; */
  width: 100%;
  /* max-width: 1200px;   */
  margin: 0 auto;     /* 좌우 중앙 정렬 */
  display: flex;
  align-items: center;
  
}
</style>
