<template>
  <v-container >
    <v-card max-width="400">
      <h1 class="text-center">로그인 테스트</h1>

      <v-form ref="loginForm" @submit.prevent="doLogin">
        <v-text-field v-model="email" label="email" outlined dense class="mb-4" required :rules="[
          (v) => !!v || '이메일을 입력하세요.',
          (v) => /.+@.+\..+/.test(v) || '유효한 이메일 주소가 아닙니다.'
        ]" />
        <v-text-field v-model="password" label="Password" type="password" outlined dense class="mb-6" required :rules="[
          (v) => !!v || '비밀번호를 입력하세요.',
          (v) => v.length >= 6 || '비밀번호는 최소 6자 이상이어야 합니다.'
        ]" />

        <v-btn block color="primary" type="submit" :disabled="!email || !password" :loading="loading" class="mb-6">
          로그인
        </v-btn>
      </v-form>

      <div v-if="auth.isLoggined" class="text-center">
        🎉 환영합니다, {{ auth.userInfo.name }} 님!
        <v-btn text color="error" @click="auth.logout">로그아웃</v-btn>
      </div>
    </v-card>
  </v-container>
</template>

<script setup>

import { ref } from 'vue'
import { useAuth } from '@/hooks/useAuth'

const auth = useAuth()
const email = ref('')
const password = ref('')
const loading = ref(false)

const doLogin = async () => {
  if (!email.value || !password.value) return
  loading.value = true
  const ok = await auth.login({ email: email.value, password: password.value })
  loading.value = false
  if (ok) {
    // 로그인 성공 후 동작
  }
}
</script>

<style scoped>
</style>

<route lang="yaml">
meta:
  layout: default
</route>
