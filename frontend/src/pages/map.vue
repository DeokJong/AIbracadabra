<template>
  <MapSearchBar />
  <KakaoMapWrapper />
  <DetailSidebar />
</template>

<script setup lang="ts">
import KakaoMapWrapper from '@/components/map/kakaoMap.vue'
import MapSearchBar from '@/components/map/MapSearchBar.vue'
import DetailSidebar from '@/components/map/DetailSidebar.vue';
</script>

<route lang="yaml">
meta:
  layout: MapLayout
</route>
