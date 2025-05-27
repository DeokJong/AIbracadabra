<template>
  <!-- 카테고리 네비게이션 바: 상단 중앙 -->
  <nav class="category-nav">
    <v-btn
      class="nav-btn"
      color="primary"
      variant="elevated"
      @click="contentSearch(ContentType.ACCOMMODATION)"
      prepend-icon="mdi-home"
    >숙소</v-btn>
    <v-btn
      class="nav-btn"
      color="deep-orange"
      variant="flat"
      @click="contentSearch(ContentType.CULTURE_FACILITY)"
      prepend-icon="mdi-theater"
    >문화시설</v-btn>
    <v-btn
      class="nav-btn"
      color="deep-purple"
      variant="flat"
      @click="contentSearch(ContentType.FESTIVAL_EVENT)"
      prepend-icon="mdi-party-popper"
    >축제정보</v-btn>
    <v-btn
      class="nav-btn"
      color="indigo"
      variant="flat"
      @click="contentSearch(ContentType.LEISURE_SPORTS)"
      prepend-icon="mdi-ski"
    >레포츠</v-btn>
    <v-btn
      class="nav-btn"
      color="light-blue"
      variant="flat"
      @click="contentSearch(ContentType.RESTAURANT)"
      prepend-icon="mdi-silverware-fork-knife"
    >식사</v-btn>
    <v-btn
      class="nav-btn"
      color="pink"
      variant="flat"
      @click="contentSearch(ContentType.SHOPPING)"
      prepend-icon="mdi-shopping"
    >쇼핑</v-btn>
    <v-btn
      class="nav-btn"
      color="teal"
      variant="flat"
      @click="contentSearch(ContentType.TOURIST_SPOT)"
      prepend-icon="mdi-map-marker-radius"
    >여행명소</v-btn>
    <v-btn
      class="nav-btn"
      color="blue-grey"
      variant="flat"
      @click="contentSearch(ContentType.TRAVEL_COURSE)"
      prepend-icon="mdi-map"
    >여행코스</v-btn>
    <v-btn
      class="nav-btn"
      style="background: linear-gradient(90deg,#ff00ff 0%,#00eaff 100%); color: white;"
      variant="flat"
      @click="fetchHotPlaces"
      prepend-icon="mdi-fire"
    >인기코스</v-btn>
  </nav>

  <!-- 검색바: 오른쪽 상단 고정 -->
  <div class="right-search-bar">
    <v-text-field
      v-model="searchKeyword"
      placeholder="검색어를 입력하세요"
      density="compact"
      hide-details
      class="search-input"
      variant="solo"
      @keyup.enter="onSearch"
    />
    <v-btn
      color="primary"
      class="search-btn"
      height="40"
      variant="flat"
      @click="onSearch"
      prepend-icon="mdi-magnify"
    >검색</v-btn>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useKakaoMap, ContentType } from '@/hooks/useKakaoMap'
import useHotPlace from '@/hooks/useHotPlace'

const { locationSearch, contentSearch } = useKakaoMap()
const { fetchHotPlaces } = useHotPlace()
const searchKeyword = ref('')

async function onSearch() {
  if (!searchKeyword.value.trim()) return
  await locationSearch(searchKeyword.value)
}
</script>

<style scoped>
.category-nav {
  position: fixed;
  top: 80px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  border-radius: 28px;
  box-shadow: 0 4px 24px rgba(102,126,234,0.10);
  padding: 16px 24px;
  z-index: 1100;
  pointer-events: auto;
}

.nav-btn {
  min-width: 110px;
  height: 44px;
  border-radius: 18px !important;
  font-weight: 600;
  font-size: 1.07rem;
  letter-spacing: 0.01em;
  box-shadow: 0 2px 10px rgba(102, 126, 234, 0.08);
  transition: transform 0.18s, box-shadow 0.18s;
  background: linear-gradient(90deg, rgba(255,255,255,0.08) 0%, rgba(230,230,250,0.22) 100%);
  pointer-events: auto;
}

.nav-btn:hover, .nav-btn:focus {
  transform: translateY(-2px) scale(1.04);
  box-shadow: 0 6px 24px rgba(102, 126, 234, 0.16);
  filter: brightness(1.07);
  opacity: 0.98;
}

.right-search-bar {
  position: fixed;
  top: 80px;
  right: 32px;
  z-index: 100;
  display: flex;
  align-items: center;
  background: rgba(255,255,255,0.96);
  border-radius: 32px;
  box-shadow: 0 4px 24px rgba(102,126,234,0.10);
  padding: 6px 18px 6px 12px;
  gap: 8px;
  min-width: 260px;
  pointer-events: auto;
}

.search-input {
  border-radius: 18px !important;
  font-size: 1.06rem;
  background: rgba(245,247,250,0.9);
  transition: box-shadow 0.2s;
  min-width: 160px;
  max-width: 220px;
}

.search-btn {
  border-radius: 18px !important;
  font-weight: 600;
  font-size: 1.05rem;
  letter-spacing: 0.02em;
  box-shadow: 0 2px 10px rgba(102, 126, 234, 0.08);
  transition: transform 0.18s, box-shadow 0.18s;
}

.search-btn:hover, .search-btn:focus {
  transform: translateY(-2px) scale(1.04);
  box-shadow: 0 6px 24px rgba(102, 126, 234, 0.16);
  filter: brightness(1.07);
  opacity: 0.98;
}

/* 반응형 */
@media (max-width: 1100px) {
  .category-nav {
    top: 68px;
    padding: 10px 6px;
    border-radius: 18px;
  }
  .nav-btn {
    min-width: 90px;
    font-size: 0.98rem;
    height: 40px;
    border-radius: 14px !important;
  }
  .right-search-bar {
    top: 68px;
    right: 10px;
    border-radius: 20px !important;
    padding: 4px 8px 4px 8px;
    min-width: 0;
  }
}

@media (max-width: 600px) {
  .category-nav {
    top: 56px;
    gap: 7px;
    padding: 4px 2px;
    border-radius: 12px;
  }
  .nav-btn {
    min-width: 74px;
    font-size: 0.91rem;
    height: 34px;
    border-radius: 8px !important;
    padding: 0 6px;
  }
  .right-search-bar {
    top: 104px;
    right: 2vw;
    border-radius: 12px !important;
    padding: 2px 4px 2px 4px;
  }
  .search-input {
    min-width: 90px;
    max-width: 120px;
    font-size: 0.95rem;
    border-radius: 8px !important;
  }
  .search-btn {
    font-size: 0.93rem;
    border-radius: 8px !important;
    height: 32px;
    min-width: 50px;
    padding: 0 6px;
  }
}

</style>
