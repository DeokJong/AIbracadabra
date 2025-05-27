// src/hooks/useHeroCarousel.ts
import { ref, onMounted, onBeforeUnmount } from 'vue'

export function useHeroCarousel(intervalMs = 5000) {
  const images = ref<string[]>([])
  const currentIndex = ref(0)
  let timer: ReturnType<typeof setInterval>

  // ▼ 반드시 리터럴 패턴으로 glob!
  const modules = import.meta.glob<string>(
    '/src/assets/*.jpg',   // <- 여기만 변경하면 됩니다
    { eager: true, as: 'url' }
  )
  images.value = Object.values(modules)

  onMounted(() => {
    timer = setInterval(() => {
      if (images.value.length > 0) {
        currentIndex.value =
          (currentIndex.value + 1) % images.value.length
      }
    }, intervalMs)
  })

  onBeforeUnmount(() => {
    clearInterval(timer)
  })

  return { images, currentIndex }
}
