<template>
  <div class="popup-wrapper">
    <v-card
      class="pa-3 popup-card"
    >
      <v-card-text class="text-center">
        <span style="font-weight: bold;">여기 핫플로 등록할래?</span>
      </v-card-text>
      <v-card-actions class="justify-center">
        <v-btn small color="primary" @click="goToRegister">등록하기</v-btn>
      </v-card-actions>
    </v-card>
    <div class="popup-tail" />
  </div>
</template>


<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();

const goToRegister = () => {
  router.push('/hotplace/register');  // 원하는 경로로 수정 가능
};
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
