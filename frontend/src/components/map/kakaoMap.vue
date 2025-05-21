<!-- src/components/kakaoMap.vue -->
<template>
  <KakaoMap v-bind="kakaoMapProps" :draggable="true"  @onLoadKakaoMap="onLoad">
  </KakaoMap>

  <v-btn
  @click="contentSearch(ContentType.TOURIST_SPOT)"
  >
  버튼 콜링
  </v-btn>
</template>

<script setup lang="ts">
import { KakaoMap } from 'vue3-kakao-maps'
import { useKakaoMap, ContentType } from '@/hooks/useKakaoMap'
import { storeToRefs } from 'pinia'
import { onMounted, ref, watch } from 'vue'
import debounce from 'lodash-es/debounce'

const { kakaoMapProps } = storeToRefs(useKakaoMap())
const { locationSearch, contentSearch } = useKakaoMap()

onMounted(() => {
  locationSearch("서울 특별시")
})

const mapRef = ref<kakao.maps.Map>()

const debouncedUpdate = debounce((map: kakao.maps.Map) => {
  const center = map.getCenter()
  kakaoMapProps.value.lng   = center.getLng()
  kakaoMapProps.value.lat   = center.getLat()
  kakaoMapProps.value.level = map.getLevel()
}, 1500)

function onLoad(map: kakao.maps.Map) {
  mapRef.value = map
  kakao.maps.event.addListener(map, 'idle', () => {
    debouncedUpdate(map)
  })
}

</script>

<style scoped></style>
