// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import { routes } from 'vue-router/auto-routes'
import { setupLayouts } from 'virtual:generated-layouts'

import { useAuth } from '@/hooks/useAuth'

const router = createRouter({
  history: createWebHistory(),
  routes: setupLayouts(routes),
})

// 로그인이 되어있으면 접근 불가능한 페이지
router.beforeEach(async (to, _, next) => {
  const auth = useAuth()
  await auth.me()
  if ((to.path === '/login' || to.path === '/signup') && auth.isLoggined) {
    return next('/')
  }

  if (to.path === '/mypage' && !auth.isLoggined) {
    return next('/login')
  }

  next()
})

export default router
