<!-- src/App.vue -->
<template>
  <v-app>
    <router-view />
    <ModalRenderer />
  </v-app>
</template>

<script setup>
import ModalRenderer from '@/components/modal/ModalRenderer.vue';

</script>

<style scoped>
/* 필요하면 여기에 스타일 추가 */
</style>
