import './assets/fonts.css'

import 'vuetify/styles'

import '@mdi/font/css/materialdesignicons.css'


import { createApp } from 'vue'



import Toast from 'vue-toastification'
import 'vue-toastification/dist/index.css'


import axios from 'axios'

import router from './router'

import App from '@/App.vue'

import { useKakao } from 'vue3-kakao-maps/@utils'
import { vuetify } from '@/plugin/vuetify'
import { pinia } from '@/plugin/pinia'

// axios 기본 설정
axios.defaults.baseURL = import.meta.env.VITE_API_BASE_URL
axios.defaults.headers.common['Content-Type'] = 'application/json'
axios.defaults.headers.common['Accept'] = 'application/json'
axios.defaults.withCredentials = true

// vuetify 설정


useKakao(
  import.meta.env.VITE_KAKAO_MAP_API,
  ['services']
)

const app = createApp(App)
app.use(pinia)
app.use(vuetify)
app.use(Toast, {
  // toast 기본 옵션 설정
  position: 'top-right',
  timeout: 2000,
})
app.use(router)
app.mount('#app')

declare module '@vue/runtime-core' {
  export interface ComponentCustomProperties {
      $goto: any
  }
}
