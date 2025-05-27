<template>
  <v-row justify="center">
    <v-col cols="auto">
      <div class="profile-card-wrapper">
        <v-card class="profile-card elevation-12">
          <div class="card-header">
            <div class="header-icon">
              <v-icon size="32" color="white">mdi-account-edit</v-icon>
            </div>
            <v-card-title class="card-title">내 정보 수정</v-card-title>
          </div>

          <v-divider class="elegant-divider" />

          <v-form ref="formRef" v-model="valid" lazy-validation @submit.prevent="doUpdate">
            <v-card-text class="form-content">
              <div class="input-group">
                <v-text-field 
                  v-model="form.email" 
                  label="이메일" 
                  variant="outlined"
                  class="elegant-input"
                  :rules="emailRules"
                  prepend-inner-icon="mdi-email" 
                  readonly 
                />
              </div>

              <div class="input-group">
                <v-text-field 
                  v-model="form.name" 
                  label="이름" 
                  variant="outlined"
                  class="elegant-input"
                  :rules="[(v) => !!v || '이름을 입력하세요.']" 
                  prepend-inner-icon="mdi-account" 
                  required 
                />
              </div>

              <div class="password-section">
                <PasswordTextField v-model="form.currentPassword" :label-name="'현재 비밀번호'" :is-required="true" />
                <PasswordTextField v-model="form.newPassword" :label-name="'새 비밀번호'" :is-required="false" />
                <PasswordTextField v-model="form.confirmPassword" :label-name="'새 비밀번호 확인'" :is-required="false" />
              </div>
            </v-card-text>

            <v-card-actions class="action-section">
              <v-btn 
                block 
                color="primary" 
                type="submit" 
                :loading="loading" 
                :disabled="!valid || loading"
                class="update-btn"
                size="large"
              >
                <v-icon left>mdi-content-save</v-icon>
                정보 수정
              </v-btn>
            </v-card-actions>
          </v-form>

          <v-divider class="elegant-divider" />
          
          <v-card-actions class="danger-section">
            <v-btn 
              block 
              color="error" 
              @click="openWithdrawModal"
              class="withdraw-btn"
              variant="outlined"
              size="large"
            >
              <v-icon left>mdi-account-remove</v-icon>
              회원 탈퇴
            </v-btn>
          </v-card-actions>
        </v-card>
      </div>
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
import MyBoards from '@/components/board/MyBoards.vue'
import MyComments from '@/components/comment/MyComments.vue'

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
<style scoped>
.profile-card-wrapper {
  perspective: 1000px;
}

.profile-card {
  width: 420px;
  border-radius: 20px;
  background: white;
  overflow: hidden;
  transition: all 0.3s ease;
  border: none;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
}

.profile-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 25px 80px rgba(0, 0, 0, 0.15);
}

.card-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 24px;
  text-align: center;
  position: relative;
}

.header-icon {
  background: rgba(255, 255, 255, 0.2);
  width: 64px;
  height: 64px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
  backdrop-filter: blur(10px);
}

.card-title {
  color: white;
  font-size: 24px;
  font-weight: 600;
  margin: 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.elegant-divider {
  opacity: 0.1;
  margin: 0;
}

.form-content {
  padding: 32px;
  background: #fafbfc;
}

.input-group {
  margin-bottom: 20px;
}

.password-section {
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid #eee;
}

.elegant-input {
  border-radius: 12px;
}

.elegant-input :deep(.v-field) {
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.elegant-input :deep(.v-field:hover) {
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.1);
}

.action-section {
  padding: 24px 32px 16px;
  background: #fafbfc;
}

.update-btn {
  background: linear-gradient(45deg, #667eea, #764ba2) !important;
  border-radius: 12px;
  font-weight: 600;
  text-transform: none;
  letter-spacing: 0.5px;
  transition: all 0.3s ease;
}

.update-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
}

.danger-section {
  padding: 16px 32px 32px;
  background: #fafbfc;
}

.withdraw-btn {
  border-radius: 12px;
  font-weight: 600;
  text-transform: none;
  letter-spacing: 0.5px;
  transition: all 0.3s ease;
}

.withdraw-btn:hover {
  background: #ff5252 !important;
  color: white !important;
  transform: translateY(-1px);
  box-shadow: 0 6px 20px rgba(255, 82, 82, 0.3);
}
</style>