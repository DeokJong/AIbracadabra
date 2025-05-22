<!-- src/components/map/kakaoMap.vue -->
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
import debounce from 'lodash-es/debounce'

const { kakaoMapProps, markerProps, currentContent } = storeToRefs(useKakaoMap())
const { locationSearch, contentDetailSearch } = useKakaoMap()

// 초기 위치 검색
onMounted(() => {
  locationSearch('서울 특별시')
})

const mapRef = ref<kakao.maps.Map>()

// 지도가 idle 상태가 될 때마다 center/level 업데이트 (디바운스 적용)
const debouncedUpdate = debounce((map: kakao.maps.Map) => {
  const center = map.getCenter()
  kakaoMapProps.value.lng = center.getLng()
  kakaoMapProps.value.lat = center.getLat()
  kakaoMapProps.value.level = map.getLevel()
}, 1000)

function onLoad(map: kakao.maps.Map) {
  mapRef.value = map
  kakao.maps.event.addListener(map, 'idle', () => {
    debouncedUpdate(map)
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
