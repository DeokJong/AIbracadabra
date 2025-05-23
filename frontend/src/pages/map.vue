<template>
  <MapSearchBar />
  <KakaoMapWrapper />
  <DetailSidebar />
  <ResultNavigationBar />
  <TripPlanSideBar />
</template>

<script setup lang="ts">
import KakaoMapWrapper from '@/components/map/KakaoMap.vue'
import MapSearchBar from '@/components/map/MapSearchBar.vue'
import DetailSidebar from '@/components/map/DetailSidebar.vue'
import ResultNavigationBar from '@/components/map/ResultNavigationBar.vue'
</script>

<route lang="yaml">
meta:
  layout: MapLayout
</route>
