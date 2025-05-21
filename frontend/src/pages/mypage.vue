<template>
  <v-row justify="center">
    <v-col cols="auto">
      <v-card width="360" class="border-1 elevation-6">
        <v-card-title class="text-h5">내 정보 수정</v-card-title>

        <v-divider thickness="2" class="my-3" opacity="0.3" />

        <v-form ref="formRef" v-model="valid" lazy-validation @submit.prevent="doUpdate">
          <v-card-text>
            <v-text-field v-model="form.email" label="이메일" outlined dense class="ma-4" :rules="emailRules"
              prepend-inner-icon="mdi-email" readonly />

            <v-text-field v-model="form.name" label="이름" outlined dense class="ma-4"
              :rules="[(v) => !!v || '이름을 입력하세요.']" prepend-inner-icon="mdi-account" required />

            <PasswordTextField v-model="form.currentPassword" :label-name="'현재 비밀번호'" :is-required="true" />

            <PasswordTextField v-model="form.newPassword" :label-name="'새 비밀번호'" :is-required="false" />

            <PasswordTextField v-model="form.confirmPassword" :label-name="'새 비밀번호 확인'" :is-required="false" />
          </v-card-text>

          <v-card-actions class="pa-4 justify-center">
            <v-btn block color="primary" type="submit" :loading="loading" :disabled="!valid || loading">
              정보 수정
            </v-btn>
          </v-card-actions>

        </v-form>

        <v-divider thickness="2" class="ma-3 ga-3" opacity="0.3" />
        <v-card-actions class="pa-4 justify-center">
          <v-btn block color="error" @click="openWithdrawModal">
            회원 탈퇴
          </v-btn>
        </v-card-actions>

      </v-card>
    </v-col>
  </v-row>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useToast } from 'vue-toastification'
import { storeToRefs } from 'pinia'

import { useModal } from '@/hooks/useModal'
import { useAuth, UpdateRequest } from '@/hooks/useAuth'
import { useValidationRules } from '@/hooks/useValidationRules'
import WithdrawModal from '@/components/modal/WithdrawModal.vue'
import PasswordTextField from '@/components/common/PasswordTextField.vue'

const { addModal } = useModal()
const auth = useAuth()
const { userInfo } = storeToRefs(auth)
const { emailRules } = useValidationRules()
const toast = useToast()

const form = reactive<UpdateRequest>({
  email: '',
  name: '',
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const valid = ref(false)
const loading = ref(false)
const formRef = ref<any>(null)

onMounted(() => {
  // 마운트시 필수 정보 기입
  form.email = userInfo.value.email
  form.name = userInfo.value.name
})


const doUpdate = async () => {
  // 업데이트시 실행시킬 함수
  if (!formRef.value?.validate()) return

  if (form.newPassword && form.confirmPassword !== form.confirmPassword) {
    toast.error('비밀번호가 일치하지 않습니다')
    return
  }

  loading.value = true
  const ok = await auth.updateUserInfo({
    ...form
  })
  loading.value = false

  if (ok) {
    form.currentPassword = ''
    form.newPassword = ''
    form.confirmPassword = ''
    formRef.value.resetValidation()
  }
}

const openWithdrawModal = async () => {
  try {
    await addModal<void>(WithdrawModal)
  } catch {
  }
}

</script>
