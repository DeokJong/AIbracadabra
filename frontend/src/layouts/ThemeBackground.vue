<template>
  <v-sheet class="theme-background d-flex flex-column" height="100vh" elevation="0">
    <HeadNav />

    <v-row class="flex-grow-1" justify="center" align="center">
      <router-view />
    </v-row>
  </v-sheet>
</template>


<script setup>
import HeadNav from '@/components/common/HeadNav.vue'
</script>

<style scoped>
.theme-background {
  background-image: url('/images/thema_photo.png');
    background-repeat: no-repeat;
  background-size: cover;
  background-position: center center;
}
</style>
