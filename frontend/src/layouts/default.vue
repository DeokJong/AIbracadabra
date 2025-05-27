<!--layouts/default.vue -->
<template>
  <div class="narrow-container">
    <HeadNav :isNarrow="true"/>
  </div>
    <v-main>
      <v-container class="py-10 narrow-container">
        <router-view />
      </v-container>
    </v-main>
  <div class="narrow-container">
    <Footer />
  </div>
</template>

<script setup>
import Footer from '@/components/common/Footer.vue';
import HeadNav from '@/components/common/HeadNav.vue'
</script>

<style scoped>
.narrow-container {
  width: 100%;
  max-width: 1200px;   
  margin: 0 auto;
}
</style>
