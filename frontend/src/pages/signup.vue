<template>
  <v-container fluid class="py-12 d-flex justify-center">
    <v-row justify="center">
      <v-col cols="12" sm="8" md="6" lg="4" xl="3" class="d-flex justify-center">

        <v-card width="100%" max-width="800" class="pa-6">
          <h1 class="text-center mb-6">회원가입</h1>

          <v-form ref="formRef" v-model="valid" lazy-validation @submit.prevent="doSignup">
            <!-- 이메일 -->
            <v-text-field v-model="form.email" label="이메일" outlined dense class="mb-4" :rules="emailRules"
              prepend-inner-icon="mdi-email" required />

            <!-- 이름 -->
            <v-text-field v-model="form.name" label="이름" outlined dense class="mb-4"
              :rules="[(v) => !!v || '이름을 입력하세요.']" prepend-inner-icon="mdi-account" required />

            <v-text-field
            v-model="form.password"
            label="비밀번호"
            :type="showPassword ? 'text' : 'password'"
            outlined dense class="mb-6"
            :rules="passwordRules"
            prepend-inner-icon="mdi-lock"
            :append-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'" @click:append="showPassword = !showPassword"/>

            <!-- 비밀번호 확인 -->
            <v-text-field
            v-model="form.checkPassword"
            label="비밀번호 확인"
            :type="showPasswordCheck ? 'text' : 'password'"
            outlined dense class="mb-6"
            :rules="passwordRules"
            prepend-inner-icon="mdi-lock-check"
            :append-icon="showPasswordCheck ? 'mdi-eye-off' : 'mdi-eye'" @click:append="showPasswordCheck = !showPasswordCheck"/>

            <!-- 가입 버튼 -->
            <v-btn block color="primary" type="submit" :loading="loading" :disabled="!valid || loading" class="mb-4">
              가입하기
            </v-btn>

            <!-- 로그인 페이지로 이동 -->
            <v-btn block color="secondary" to="/login" :disabled="loading">
              로그인으로 이동
            </v-btn>
          </v-form>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useValidationRules } from '@/hooks/useValidationRules'
import router from '@/router'
import { useAuth, SignUpRequest } from '@/hooks/useAuth'

const { emailRules, passwordRules } = useValidationRules()
const { signup } = useAuth()


const showPassword = ref(false)
const showPasswordCheck = ref(false)

const form = reactive<SignUpRequest>({
  email: '',
  password: '',
  checkPassword: '',
  name: '',

})

const valid = ref(false)
// 여기를 간단하게
const formRef = ref<any>(null)

const loading = ref(false)

const doSignup = async () => {
  // v-form 유효성 검사
  if (!formRef.value?.validate()) return

  loading.value = true
  const ok = await signup(form)
  if (ok) {
    router.push('/login')
  }
  loading.value = false
}
</script>


<route lang="yaml">
meta:
  layout: ThemeBackground
</route>

<style scoped>
/* (필요시 추가 스타일) */
</style>
