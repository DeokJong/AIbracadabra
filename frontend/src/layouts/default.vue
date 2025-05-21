<!--layouts/default.vue -->
<!--
  이 파일은 Vue.js 애플리케이션의 기본 레이아웃을 정의합니다.
  이 레이아웃은 헤더와 메인 콘텐츠 영역을 포함하고 있습니다.
  하위 컴포넌트들은 row 구조로 배치되어 있으며,
  중앙 정렬된 flexbox 레이아웃을 사용하여 콘텐츠를 배치합니다.
-->

<template>
  <HeadNav />
    <v-main>
      <v-container class="py-10">
        <router-view />
      </v-container>
    </v-main>

</template>

<script setup>
import HeadNav from '@/components/common/HeadNav.vue'

</script>
