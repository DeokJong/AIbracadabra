
<template>
  <div class="not-found">
    <h1>😢 404 Not Found</h1>
    <p>요청하신 페이지를 찾을 수 없습니다.</p>
    <router-link to="/">홈으로 돌아가기</router-link>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.not-found {
  text-align: center;
  margin-top: 4rem;
}
.not-found h1 {
  font-size: 2rem;
  margin-bottom: 1rem;
}
</style>
