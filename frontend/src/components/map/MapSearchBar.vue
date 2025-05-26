<template>
  <div class="center-bar">
    <div class="d-flex justify-center flex-wrap">
      <v-btn class="nav-btn" color="red" @click="contentSearch(ContentType.ACCOMMODATION)">숙소</v-btn>
      <v-btn class="nav-btn" color="deep-orange" @click="contentSearch(ContentType.CULTURE_FACILITY)">문화시설</v-btn>
      <v-btn class="nav-btn" color="deep-purple" @click="contentSearch(ContentType.FESTIVAL_EVENT)">축제정보</v-btn>
      <v-btn class="nav-btn" color="indigo" @click="contentSearch(ContentType.LEISURE_SPORTS)">레포츠</v-btn>
      <v-btn class="nav-btn" color="light-blue" @click="contentSearch(ContentType.RESTAURANT)">식사</v-btn>
      <v-btn class="nav-btn" color="pink" @click="contentSearch(ContentType.SHOPPING)">쇼핑</v-btn>
      <v-btn class="nav-btn" color="teal" @click="contentSearch(ContentType.TOURIST_SPOT)">여행명소</v-btn>
      <v-btn class="nav-btn" color="blue-grey" @click="contentSearch(ContentType.TRAVEL_COURSE)">여행코스</v-btn>
    </div>
  </div>

<div>
    <v-card
      flat
      class="right-search-bar d-flex align-center pa-2 bg-white"
      style="border-radius: 32px; z-index: 999;"
      variant="outlined"
      >
      <!-- 검색 입력 -->
      <v-text-field
        v-model="searchKeyword"
        placeholder="검색어를 입력하세요"
        dense
        outlined
        hide-details
        class="flex-grow-1"
        width="200"
        @keyup.enter="onSearch"
      />

      <v-btn
        color="primary"
        class="ml-2"
        height="40"
        @click="onSearch"
      >
        검색
      </v-btn>
    </v-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useKakaoMap, ContentType } from '@/hooks/useKakaoMap'

const { locationSearch, contentSearch } = useKakaoMap()

const searchKeyword = ref('')

async function onSearch() {
  if (!searchKeyword.value.trim()) return
  await locationSearch(searchKeyword.value)
}
</script>

<style scoped>
.center-bar {
  position: fixed;
  top: 80px;               /* 상단에서 80px 아래 */
  left: 50%;
  transform: translateX(-50%);
  z-index: 1000;
}

.right-search-bar {
  position: fixed;
  top: 80px;               /* .center-bar 과 같은 top 값 */
  right: 16px;             /* 화면 오른쪽에서 16px 안쪽 */
  z-index: 1000;
  height: 40px;      /* 높이를 고정 */
  margin: 8px;       /* 버튼 간격 (ma-2 ≒ 8px) */
}

.nav-btn {
  min-width: 100px;  /* 너비를 고정 */
  height: 40px;      /* 높이를 고정 */
  margin: 8px;       /* 버튼 간격 (ma-2 ≒ 8px) */
}

</style>
