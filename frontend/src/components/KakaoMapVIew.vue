<!-- src/components/kakaoMap.vue -->
<template>
  <div class="map-container">
    <KakaoMap
      class="map"
      :lat="center.lat"
      :lng="center.lng"
      :level="3"
      :draggable="true"
      @onLoadKakaoMap="onLoad"
    >
      <KakaoMapMarker
        v-for="(m, i) in markers"
        :key="i"
        :lat="m.lat"
        :lng="m.lng"
        :infoWindow="m.infoWindow"
        :clickable="true"
        @onClickKakaoMapMarker="() => onMarkerClick(i)"
      />
    </KakaoMap>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { KakaoMap, KakaoMapMarker } from 'vue3-kakao-maps'

const props = defineProps({
  keyword: { type: String, required: true }
})

const center = ref({ lat: 33.450701, lng: 126.570667 })
const map = ref<any>(null)
const markers = ref<Array<{ lat: number; lng: number; infoWindow: { content: string; visible: boolean } }>>([])

function onLoad(m: any) {
  map.value = m
  doSearch(props.keyword)
}

watch(() => props.keyword, (newKw) => {
  if (map.value) doSearch(newKw)
})

function doSearch(kw: string) {
  markers.value = []
  const ps = new kakao.maps.services.Places()
  ps.keywordSearch(kw, (data: any, status: any) => {
    if (status === kakao.maps.services.Status.OK) {
      const bounds = new kakao.maps.LatLngBounds()
      data.forEach((place: any) => {
        const content = `
          <div style="padding:5px; font-size:14px;">
            <strong>${place.place_name}</strong><br/>
            ${place.road_address_name || place.address_name}<br/>
            ${place.phone || '전화번호 정보 없음'}
          </div>`
        markers.value.push({
          lat: place.y,
          lng: place.x,
          infoWindow: { content, visible: false }
        })
        bounds.extend(new kakao.maps.LatLng(place.y, place.x))
      })
      map.value.setBounds(bounds)
      center.value = { lat: data[0].y, lng: data[0].x }
    }
  })
}

function onMarkerClick(idx: number) {
  markers.value.forEach((m, i) => {
    m.infoWindow.visible = i === idx ? !m.infoWindow.visible : false
  })
}
</script>

<style scoped>
.map-container {
  width: 100%;
  height: 60vh;      /* Viewport 높이의 60% */
  min-height: 300px;
  max-height: 80vh;  /* 최대 높이를 화면의 80%까지 */
  position: relative;
}

.map {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
}
</style>
