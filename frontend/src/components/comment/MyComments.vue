<!-- src/components/comment/MyComments.vue -->
<template>
  <div>
    <h2 class="text-xl font-semibold mb-4">내가 쓴 댓글</h2>

    <table class="min-w-full border-collapse mb-4">
      <thead>
        <tr class="bg-gray-100">
          <th class="px-4 py-2 text-left">게시글 번호</th>
          <th class="px-4 py-2 text-left">작성자</th>
          <th class="px-4 py-2 text-left">내용</th>
          <th class="px-4 py-2 text-left">작성일</th>
          <th class="px-4 py-2 text-left">수정일</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="comment in items"
          :key="comment.cno"
          class="border-t hover:bg-gray-50 cursor-pointer"
          @click="onRowClick(comment)"
        >
          <td class="px-4 py-2">{{ comment.bno }}</td>
          <td class="px-4 py-2">{{ comment.author }}</td>
          <td class="px-4 py-2 break-words">{{ comment.content }}</td>
          <td class="px-4 py-2">{{ comment.createdDate }}</td>
          <td class="px-4 py-2">{{ comment.updatedDate || '-' }}</td>
        </tr>
        <tr v-if="!loading && items.length === 0">
          <td class="px-4 py-6 text-center" colspan="5">
            작성된 댓글이 없습니다.
          </td>
        </tr>
      </tbody>
    </table>

    <div class="flex justify-center items-center space-x-2">
      <button
        class="px-3 py-1 border rounded disabled:opacity-50"
        :disabled="currentPage <= 1 || loading"
        @click="currentPage--"
      >
        이전
      </button>
      <span>{{ currentPage }} / {{ pages }}</span>
      <button
        class="px-3 py-1 border rounded disabled:opacity-50"
        :disabled="currentPage >= pages || loading"
        @click="currentPage++"
      >
        다음
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useMyComments } from '@/hooks/useMyBoards'
import { useRouter } from 'vue-router'
import type { CommentSummary } from '@/hooks/useMyBoards'

const router = useRouter()
const { items, pages, currentPage, loading } = useMyComments()

function onRowClick(comment: CommentSummary) {
  // 상세 페이지가 /board/:bno 라면:
  router.push(`/board/${comment.bno}`)
  // Q&A 게시판 전용이면: router.push(`/qna/${comment.bno}`)
}
</script>

<style scoped>
table { border: 1px solid #e2e8f0; }
th, td { border-bottom: 1px solid #e2e8f0; }
.cursor-pointer { cursor: pointer; }
</style>
