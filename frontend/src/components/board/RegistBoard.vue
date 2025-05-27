<template>
  <v-container class="py-6">
    <v-card class="pa-6" max-width="800" elevation="3">
      <!-- 제목 + 공개/비공개 토글만 여기 -->
      <v-card-title class="d-flex align-center justify-space-between">
        <span class="text-h5 font-weight-bold">게시글 작성</span>
        <v-radio-group v-model="form.visibility" row>
          <v-radio label="공개" value="PUBLIC" />
          <v-radio label="비공개" value="PRIVATE" />
        </v-radio-group>
      </v-card-title>

      <v-card-text>
        <v-text-field
          label="제목"
          v-model="form.title"
          outlined
          dense
          class="mt-4"
        />
        <v-textarea
          label="내용"
          v-model="form.content"
          rows="10"
          outlined
          class="mt-4"
        />

        <!-- 파일 업로드 & 미리보기는 본문(Text) 아래 -->
        <div class="image-upload mt-4">
          <input type="file" accept="image/*" @change="onFileChange" />
          <img
            v-if="previewUrl"
            :src="previewUrl"
            class="preview mt-2"
            alt="미리보기"
          />
        </div>

        <v-btn
          color="primary"
          class="mt-4"
          :loading="loading"
          @click="submitPost"
        >
          등록
        </v-btn>
      </v-card-text>
    </v-card>
  </v-container>
</template>



<script setup lang="ts">
import { reactive, ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import { useAuth } from '@/hooks/useAuth'
import { uploadBoardImage } from '@/hooks/boardService'
import { useImageUpload } from '@/hooks/useImageUpload'

const router = useRouter()
const toast = useToast()
const auth = useAuth()

const loading = ref(false)

// boardType 대신 visibility 필드 추가
const form = reactive({
  title: '',
  content: '',
  boardType: 'board',
  visibility: 'PUBLIC'  // 기본값을 공개로 설정
})
const { file, previewUrl, onFileChange } = useImageUpload()


const submitPost = async () => {
  if (!form.title || !form.content) {
    toast.warning('제목과 내용을 모두 입력해주세요.')
    return
  }

  loading.value = true

  try {
    const postRes = await axios.post('/api/v1/board', {
      title: form.title,
      content: form.content,
      boardType: form.boardType,
      visibility: form.visibility
    })
    const bno = postRes.data.data.bno as number
    console.log(bno)

    if (file.value) {
      await uploadBoardImage(bno, file.value)
    }

    toast.success('게시글이 등록되었습니다.')
    router.push('/board')
  } catch (err) {
    toast.error('등록에 실패했습니다.')
    console.error(err)
  } finally {
    loading.value = false
  }
}
</script>
<style scoped>
.image-upload input {
  display: block;
}

.preview {
  max-width: 100%;
  max-height: 200px;
  border-radius: 8px;
  object-fit: cover;
}
</style>