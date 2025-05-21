import './assets/fonts.css'

import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import '@mdi/font/css/materialdesignicons.css'  


import { createApp } from 'vue'

import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

import Toast from 'vue-toastification'
import 'vue-toastification/dist/index.css'


import axios from 'axios'

import router from './router'

import App from '@/App.vue'

import { useKakao } from 'vue3-kakao-maps/@utils';

// axios 기본 설정
axios.defaults.baseURL = import.meta.env.VITE_API_BASE_URL
axios.defaults.headers.common['Content-Type'] = 'application/json'
axios.defaults.headers.common['Accept'] = 'application/json'
axios.defaults.withCredentials = true

// vuetify 설정
const vuetify = createVuetify({
  components,
  directives,
  theme: {
    defaultTheme: 'light',
  },
   defaults: {
    VDataTable: {
      ripple: false, // 모든 VDataTable 컴포넌트에서 ripple 끔
    },
  },
})

useKakao(
  '43daa869365f2687cdd8f64f2a4c8ec4',
  ['services']
)

const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)


const app = createApp(App)
app.use(vuetify)

app.use(Toast, {
  // toast 기본 옵션 설정
  position: 'top-right',
  timeout: 2000,
})

// pinia 설정
app.use(pinia)

app.use(router)
app.mount('#app')

declare module '@vue/runtime-core' {
  export interface ComponentCustomProperties {
      $goto: any
  }
}
