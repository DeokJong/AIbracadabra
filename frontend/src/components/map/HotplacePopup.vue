<template>
  <div class="popup-wrapper">
    <v-card class="popup-card">
      <!-- 헤더 섹션 -->
      <div class="card-header">
        <h3 class="header-title">
          <v-icon left color="white">mdi-map-marker-star</v-icon>
          핫플레이스 등록
        </h3>
      </div>

      <!-- 폼 섹션 -->
      <v-form @submit.prevent="submitForm" ref="formRef" class="form-content">
        <v-text-field
          v-model="form.hotPlace.title"
          label="제목"
          outlined
          dense
          color="deep-purple"
          prepend-inner-icon="mdi-format-title"
          :rules="[(v) => !!v || '제목 필수입니다.']"
          class="custom-input"
        />

        <v-textarea
          v-model="form.hotPlace.overview"
          label="설명"
          outlined
          dense
          color="deep-purple"
          prepend-inner-icon="mdi-text-box"
          :rules="[(v) => !!v || '설명은 필수입니다.']"
          class="custom-input"
          rows="3"
        />

        <v-file-input
          v-model="form.imageFile"
          label="이미지 업로드"
          outlined
          dense
          color="deep-purple"
          prepend-icon="mdi-image"
          accept="image/*"
          show-size
          class="custom-input"
        />

        <div class="action-buttons">
          <v-btn 
            type="submit" 
            color="primary" 
            block 
            depressed
            class="submit-btn"
          >
            <v-icon left>mdi-check-circle</v-icon>
            등록하기
          </v-btn>
          
          <v-btn 
            color="rgb(227 96 120) " 
            block 
            depressed
            @click="props.onClose?.()"
            class="close-btn"
          >
            <v-icon left>mdi-close-circle</v-icon>
            닫기
          </v-btn>
        </div>
      </v-form>
    </v-card>
    <div class="popup-tail" />
  </div>
</template>

<script setup lang="ts">
import useHotPlace, { HotPlace } from '@/hooks/useHotPlace'
import { ref } from 'vue';

const hotPlace = useHotPlace()

const props = defineProps<{
  lat: number
  lng: number
  onClose?: () => void
}>()

const submitForm = async () => {
  form.value.hotPlace.mapX = props.lng
  form.value.hotPlace.mapY = props.lat

  if (!form.value.hotPlace.title || !form.value.hotPlace.overview) {
    return
  }

  const ok = await hotPlace.registerHotPlace(form.value.hotPlace, form.value.imageFile)
  if (ok) props.onClose?.()
}

const formRef = ref()
const form = ref<{hotPlace: HotPlace, imageFile?: File}>({
  hotPlace : {} as HotPlace
})

</script>

<style scoped>
/* 전체 컨테이너 */
.popup-wrapper {
  position: relative;
  filter: drop-shadow(0 12px 28px rgba(0,0,0,0.15));
  z-index: 1000;
}

/* 카드 디자인 */
.popup-card {
  width: 320px;
  background: white;
  border-radius: 16px !important;
  overflow: hidden;
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.popup-card:hover {
  transform: translateY(-2px);
}

/* 헤더 섹션 */
.card-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 18px 24px;
}

.header-title {
  color: white;
  font-size: 1.25rem;
  font-weight: 600;
  margin: 0;
  display: flex;
  align-items: center;
}

/* 폼 내용 */
.form-content {
  padding: 20px 24px 24px;
}

.custom-input {
  margin-bottom: 16px;
}

.custom-input :deep(.v-input__slot) {
  border-radius: 12px !important;
  box-shadow: 0 2px 8px rgba(102,126,234,0.1) !important;
  transition: box-shadow 0.3s;
}

.custom-input:focus-within :deep(.v-input__slot) {
  box-shadow: 0 4px 16px rgba(102,126,234,0.2) !important;
}

/* 액션 버튼 */
.action-buttons {
  margin-top: 12px;
}

.submit-btn {
  background: linear-gradient(135deg, #4fd1c5 0%, #4299e1 100%) !important;
  color: white !important;
  border-radius: 12px !important;
  height: 44px;
  font-weight: 600;
  letter-spacing: -0.5px;
}

.close-btn {
  margin-top: 8px;
  border: 1px solid #e2e8f0 !important;
  border-radius: 12px !important;
  height: 44px;
  font-weight: 500;
  /* color: #4a5568 !important; */
}

/* 팝업 꼬리 */
.popup-tail {
  position: absolute;
  bottom: -14px;
  left: 50%;
  transform: translateX(-50%);
  width: 0;
  height: 0;
  border-left: 14px solid transparent;
  border-right: 14px solid transparent;
  border-top: 14px solid white;
  filter: drop-shadow(0 4px 4px rgba(0,0,0,0.05));
}

/* 애니메이션 */
.v-enter-active, .v-leave-active {
  transition: opacity 0.3s, transform 0.3s;
}
.v-enter-from, .v-leave-to {
  opacity: 0;
  transform: translateY(10px);
}
</style>