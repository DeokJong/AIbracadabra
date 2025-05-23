<template>
  <div>
    <!-- 토글 버튼 (오른쪽 사이드) -->
    <v-btn
      icon
      class="toggle-btn elevation-6"
      :style="{ left: drawer ? width + 'px' : '0px' }"
      @click="drawer = !drawer"
    >
      <v-icon>{{ drawer ? 'mdi-chevron-right' : 'mdi-chevron-left' }}</v-icon>
    </v-btn>

    <!-- 사이드바 본체 -->
    <div
      class="sidebar elevation-6"
      :class="{ open: drawer }"
    >
      <div class="sidebar-content">
        <v-card flat style="top: 60px;">
          <v-card-title class="headline">나의 일정</v-card-title>

          <!-- ① ScheduleItem 컴포넌트로 대체 -->
          <v-list dense>
            <ScheduleItem
              v-for="(it, idx) in planList"
              :key="it.contentId"
              :item="it"
              :index="idx"
              @remove="onRemove"
            />
          </v-list>

          <!-- 일정 제목 입력 -->
          <v-text-field
            v-model="currentPlan.title"
            label="일정 제목"
            class="mt-4"
          />

          <v-divider class="my-2"/>

          <!-- 저장 -->
          <v-btn
            block
            color="success"
            class="mt-2"
            @click="savePlan"
          >
            저장
          </v-btn>
        </v-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch,  computed } from 'vue'
import { useKakaoMap } from '@/hooks/useKakaoMap'
import ScheduleItem from '@/components/map/ScheduleItem.vue'
import { storeToRefs } from 'pinia'

const props = defineProps({
  width: { type: [Number, String], default: 300 },
})
const { width } = props

const { savePlan, removeSchedule } = useKakaoMap()
const { currentPlan } = storeToRefs(useKakaoMap())

const planList = computed(() => currentPlan.value.schedules)

function onRemove(index: number) {
  removeSchedule(index)
}

/** 사이드바 열림 상태 */
const drawer = ref(false)

watch(
  () => currentPlan.value.schedules.length,
  len => { drawer.value = len > 0 },
  { immediate: true }
)

</script>

<style scoped>
.sidebar {
  position: fixed; top:0; right:0; height:100vh;
  transform: translateX(100%); transition: transform .3s;
  background:white; overflow:auto; z-index:999;
}
.sidebar.open { transform: translateX(0); }
.sidebar-content { padding:16px; }
.toggle-btn {
  position:fixed; top:50%; left:0;
  width:16px; height:64px;
  background:white; border-radius:8px 0 0 8px;
  box-shadow:0 2px 4px rgba(0,0,0,0.1);
  display:flex; align-items:center; justify-content:center;
  z-index:999;
}
</style>
