<template>
  <v-container fluid class="py-12">
    <v-row justify="center">
      <v-col cols="12" sm="8" md="6" lg="4" xl="3" class="d-flex justify-center">
        <v-card width="100%" max-width="800" class="pa-6">
          <h1 class="text-center mb-6">AIbracadabra</h1>
          <p class="text-center mb-6">당신의 여행과 함께</p>

          <v-form ref="loginForm" @submit.prevent="doLogin">
            <v-text-field v-model="email" label="email" outlined dense class="mb-4" :rules="emailRules" prepend-inner-icon="mdi-email"/>
            <v-text-field
            v-model="password"
            label="Password"
            :type="showPassword ? 'text' : 'password'"
            outlined dense class="mb-6"
            :rules="passwordRules"
            prepend-inner-icon="mdi-lock"
            :append-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'" @click:append="showPassword = !showPassword"/>

            <v-btn block color="primary" type="submit" :disabled="!email || !password" :loading="loading" class="mb-6">
              로그인
            </v-btn>
            <v-btn block color="secondary" to="/signup" :disabled="loading" class="mb-6">
              회원가입
            </v-btn>
          </v-form>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useAuth } from '@/hooks/useAuth'
import { useValidationRules } from '@/hooks/useValidationRules'
import router from '@/router'

const { emailRules, passwordRules } = useValidationRules()
const auth = useAuth()

const email = ref('')
const showPassword = ref(false)
const password = ref('')
const loading = ref(false)


const doLogin = async () => {
  if (!email.value || !password.value) return
  loading.value = true
  const ok = await auth.login({ email: email.value, password: password.value })
  loading.value = false
  if (ok) {
    router.push('/')
  }
}
</script>

<route lang="yaml">
meta:
  layout: ThemeBackground
</route>
