<!-- src/App.vue -->
<template>
  <v-app>
    <router-view />
    <ModalRenderer />
  </v-app>
</template>

<script setup>
import ModalRenderer from '@/components/modal/ModalRenderer.vue';
import { useAuth } from './hooks/useAuth';
import { onMounted } from 'vue';
import { usePlan } from './hooks/usePlan';

const auth = useAuth()
const plan = usePlan()
onMounted(async () => {
  await auth.me()
  if (auth.isLoggined) {
    await plan.setupMyPlans()
  }
})
</script>

<style scoped>
/* 필요하면 여기에 스타일 추가 */
</style>
