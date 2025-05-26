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
import { createApp, onMounted, ref, watch } from 'vue'
import { vuetify } from '@/plugin/vuetify'
import HotPlacePopup from "@/components/map/HotPlacePopup.vue"

const { kakaoMapProps, traceMapProps, markerProps, currentContent } = storeToRefs(useKakaoMap())
const { contentDetailSearch } = useKakaoMap()

const mapRef = ref<kakao.maps.Map>()
const customOverlay = ref<kakao.maps.CustomOverlay | null>(null);

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

kakao.maps.event.addListener(map, 'rightclick', function(mouseEvent: kakao.maps.event.MouseEvent) {
  const latlng = mouseEvent.latLng;

  // 기존 오버레이 제거
  customOverlay.value?.setMap(null)

  // Vue 컴포넌트 마운트용 div 생성
  const container = document.createElement('div')
  container.id = 'hotplace-popup'

  // Vue 앱 마운트
  const popupApp = createApp(HotPlacePopup, {
    lat: latlng.getLat(),
    lng: latlng.getLng(),
    onClose: () => {
      customOverlay.value?.setMap(null)
    }
  })
  popupApp.use(vuetify)
  popupApp.mount(container)

  // 오버레이 생성 및 등록
  customOverlay.value = new kakao.maps.CustomOverlay({
    content: container,
    map: map,
    position: latlng,
    yAnchor: 1,
  })
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
.customoverlay {
  position: relative;
  bottom: 40px;
  border-radius: 6px;
  background: white;
  padding: 6px 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  font-size: 14px;
  color: #333;
  white-space: nowrap;
}
.customoverlay .title {
  font-weight: bold;
}

</style>
