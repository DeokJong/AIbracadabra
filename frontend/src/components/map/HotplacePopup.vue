<!-- src/components/map/HotPlacePopup.vue -->
<template>
  <div class="popup-wrapper">
    <v-card class="pa-3 popup-card">
      <v-form @submit.prevent="submitForm" ref="formRef">
        <v-text-field
          v-model="form.hotPlace.title"
          label="제목"
          dense
          outlined
          required
          :rules="[(v) => !!v || '제목 필수입니다.']"
        />
        <v-textarea
          v-model="form.hotPlace.overview"
          label="설명"
          dense
          outlined
          required
          :rules="[(v) => !!v || '설명은 필수입니다.']"
        />
        <v-file-input
          v-model="form.imageFile"
          label="이미지 업로드"
          dense
          outlined
          accept="image/*"
          prepend-icon="mdi-camera"
        />
        <v-btn type="submit" color="primary" block>등록하기</v-btn>
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
.popup-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  border: 1px;
  z-index: 1000;
}

.popup-card {
  width: 200px;
  background: papayawhip;
  border-radius: 8px;
  box-shadow: 0px 4px 16px rgba(0, 0, 0, 0.25);
  z-index: 1;
}

.popup-tail {
  width: 0;
  height: 0;
  margin-top: -2px; /* 그림자 겹침 줄이기 */
  border-left: 15px solid transparent;
  border-right: 15px solid transparent;
  border-top: 15px solid papayawhip;
  z-index: 2;
  position: relative;
}
</style>
