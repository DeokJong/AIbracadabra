// src/hooks/useImageUpload.ts
import { ref } from 'vue'

export function useImageUpload() {
  const file = ref<File | null>(null)
  const previewUrl = ref<string>('')

  function onFileChange(e: Event) {
    const input = e.target as HTMLInputElement
    const f = input.files?.[0] ?? null
    if (!f || !f.type.startsWith('image/')) {
      alert('이미지 파일만 업로드 가능합니다.')
      input.value = ''
      file.value = null
      previewUrl.value = ''
      return
    }
    file.value = f
    previewUrl.value = URL.createObjectURL(f)
  }

  return { file, previewUrl, onFileChange }
}
