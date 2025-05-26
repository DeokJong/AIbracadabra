// src/hooks/useAuth.ts
import { CommonResponse } from '@/service/common'
import axios, { isAxiosError } from 'axios'
import { defineStore } from 'pinia'
import { reactive, ref } from 'vue'
import { useToast } from 'vue-toastification'

export interface UserInfo {
  mno: number
  name: string
  email: string
  role: string
}

export type LoginRequest = {
  email: string
  password: string
}

export type SignUpRequest = {
  email?: string
  name?: string
  password?: string
  checkPassword?: string
}

export type UpdateRequest = {
  email: string
  name: string
  currentPassword: string
  newPassword? : string
  confirmPassword? : string
}

const GUEST_USER: UserInfo = {
  mno: -1,
  name: '',
  email: '',
  role: 'GUEST'
}

export const useAuth = defineStore(
  'auth',
  () => {
    const toast = useToast()
    const userInfo = reactive<UserInfo>({ ...GUEST_USER })
    const isLoggined = ref(false)

    const isAuthenticated = () => {
      return isLoggined.value
    }

    const me = async () => {
      try {
        const res = await axios.get<CommonResponse<UserInfo>>('/api/v1/member/me')
        const data = res.data.data

        userInfo.mno = data.mno
        userInfo.name = data.name
        userInfo.email = data.email
        userInfo.role = data.role
        isLoggined.value = true
      } catch (err) {
        isLoggined.value = false
        Object.assign(userInfo, GUEST_USER)
      }
    }

    const login = async (loginRequest: LoginRequest) => {
      try {
        await axios.post('/api/v1/auth/login', loginRequest)
        const res = await axios.get<CommonResponse<UserInfo>>('/api/v1/member/me')
        const data = res.data.data

        Object.assign(userInfo, data)

        isLoggined.value = true
        toast.success('로그인 성공')
        return true
      } catch (err) {
        if (isAxiosError(err) && err.response?.status === 401) {
          toast.info('로그인 실패')
        } else {
          toast.warning('로그인 중 에러')
        }
        return false
      }
    }

    const logout = async () => {
      try {
        await axios.post('/api/v1/auth/logout')
        isLoggined.value = false
        Object.assign(userInfo, GUEST_USER)
        toast.info('로그아웃 되었습니다')
      } catch (err) {
        if (isAxiosError(err) && err.response?.status === 401) {
          toast.info('로그아웃 실패')
        } else {
          toast.warning('로그아웃 중 에러')
        }
      } finally {
        window.location.reload()
      }
    }

    const signup = async (signupRequest: SignUpRequest) => {
      try {
        await axios.post('/api/v1/member/signup', signupRequest)
        toast.success('회원가입 성공')
        return true;
      } catch (err) {
        if (isAxiosError(err) && err.response?.status === 400) {
          toast.error('회원가입 실패')
        } else if (isAxiosError(err) && err.response?.status === 409) {
          toast.error('이미 가입된 이메일입니다')
        } else if (isAxiosError(err) && err.response?.status === 500) {
          toast.error('서버 에러')
        } else {
          toast.warning('회원가입 중 에러')
        }
      }
      return false;
    }

    const updateUserInfo = async (updateReqeust: UpdateRequest) => {
      try {
        await axios.put(`/api/v1/member`, updateReqeust)
        await me()
        toast.success("업데이트 성공")
        return true;
      } catch (err) {
        if (isAxiosError(err) && err.response?.status === 400) {
          console.log(err.response?.data)
          toast.error(err.response?.data?.error)
        } else if (isAxiosError(err) && err.response?.status === 500) {
          toast.error('서버 에러')
        } else {
          toast.warning('알 수 없는 에러')
        }
      }
      return false;
    }

    const withdraw = async () => {
      try {
        await axios.delete(`/api/v1/member`)
        await me()
        toast.success("그 동안 함께해서 즐거웠어요...")
        return true;
      } catch (err) {
        if (isAxiosError(err) && err.response?.status === 400) {
          console.log(err.response?.data)
          toast.error(err.response?.data?.error)
        } else if (isAxiosError(err) && err.response?.status === 500) {
          toast.error('아직 갈 수 없습니다... 관리자에게 문의하세요')
        } else {
          toast.warning('알 수 없는 에러')
        }
      }
      return false;
    }

    return { userInfo, isLoggined, login, logout, me, isAuthenticated, signup, updateUserInfo, withdraw }
  }
)
