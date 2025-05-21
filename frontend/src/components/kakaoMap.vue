<template>
  <SearchBar @search="onSearch" />
  <KakaoMapView :keyword="keyword" />
</template>

<script setup>
import { ref } from 'vue'
import SearchBar from './KakaoSearchBar.vue'
import KakaoMapView from './KakaoMapView.vue'

const keyword = ref('역삼역 맛집')

function onSearch(kw) {
  keyword.value = kw
}
</script>
