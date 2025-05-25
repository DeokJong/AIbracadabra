<!-- src/components/map/KakaoMap.vue -->
<template>
  <KakaoMap v-bind="kakaoMapProps" :draggable="true" @onLoadKakaoMap="onLoad" width="100%" height="100%">
    <KakaoMapMarker v-for="marker in markerProps" :key="marker.lat + marker.lng" v-bind="marker"
      @onLoadKakaoMapMarker="(markerInstance) => handleMarkerLoad(markerInstance, marker)" />
  </KakaoMap>
</template>

<script setup lang="ts">
import { KakaoMap, KakaoMapMarker } from 'vue3-kakao-maps'
import { useKakaoMap } from '@/hooks/useKakaoMap'
import { storeToRefs } from 'pinia'
import { onMounted, ref, watch } from 'vue'

const { kakaoMapProps, traceMapProps, markerProps, currentContent } = storeToRefs(useKakaoMap())
const { locationSearch, contentDetailSearch } = useKakaoMap()

// 초기 위치 검색
onMounted(() => {
  locationSearch('서울 특별시')
})

const mapRef = ref<kakao.maps.Map>()

function onLoad(map: kakao.maps.Map) {
  mapRef.value = map
  kakao.maps.event.addListener(map, 'idle', () => {
    const center = map.getCenter()
    const newLng = center.getLng(), newLat = center.getLat()
  // 변화량이 작은 소숫점 오차 범위(예: 0.000001) 안이면 무시
  if (Math.abs(traceMapProps.value.lng - newLng) < 1e-6 &&
      Math.abs(traceMapProps.value.lat - newLat) < 1e-6) {
    return
  }
  traceMapProps.value.lng = newLng
  traceMapProps.value.lat = newLat
  traceMapProps.value.level = map.getLevel()
  })
}

/** 마커가 로드된 시점에 호출됩니다. */
function handleMarkerLoad(
  markerInstance: kakao.maps.Marker,
  markerData: typeof markerProps.value[number]) {
  kakao.maps.event.addListener(markerInstance, 'click', async () => {
    await contentDetailSearch(markerData.info.contentId)
  })
}

watch(currentContent.value, () => {
  console.log(currentContent.value)
})

</script>

<style scoped>
/* 필요하다면 스타일 추가 */
</style>
