<template>
  <!-- 마우스 이벤트를 루트 엘리먼트에 걸어줍니다 -->
  <div
    class="chatbot"
    :style="{
      top: pos.y + 'px',
      left: pos.x + 'px',
      zIndex: 3000
    }"
    @mousedown="onMouseDown"
  >
    <!-- 여기를 잡고 끌어도 되고, 전체 영역을 잡아도 됩니다 -->
    <div class="drag-handle">💬 드래그해서 이동</div>

    <div class="messages">
      <div
        v-for="(msg, idx) in messages"
        :key="idx"
        :class="['message', msg.sender]"
      >
        <span v-if="msg.sender === 'user'">
          🧑 {{ auth.userInfo.name }}님: {{ msg.content }}
        </span>
        <span v-else>🤖 에브라: {{ msg.content }}</span>
      </div>
    </div>

    <div class="input-area">
      <input
        v-model="inputText"
        @keyup.enter="onSubmit"
        placeholder="메시지를 입력하세요"
      />
      <button @click="onSubmit">전송</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useChatbot } from '@/hooks/useKakaoMap'
import { useAuth } from '@/hooks/useAuth'
import { useToast } from 'vue-toastification'

const auth = useAuth()
const router = useRouter()
const toast = useToast()

const { inputText, messages, sendMessage } = useChatbot()

// 드래그용 상태
const pos = reactive({ 
  // 초기 위치: 우측 아래 (윈도우 크기 - 컴포넌트 너비)
  x: window.innerWidth - 420, 
  y: window.innerHeight - 440 
})
let dragging = false
const offset = { x: 0, y: 0 }

// 마우스 누를 때
function onMouseDown(e: MouseEvent) {
  dragging = true
  // 커서 위치와 컴포넌트 좌표 차이 계산
  offset.x = e.clientX - pos.x
  offset.y = e.clientY - pos.y
  // 드래그 중엔 선택 방지
  document.body.style.userSelect = 'none'
}

// 마우스 움직일 때
function onMouseMove(e: MouseEvent) {
  if (!dragging) return
  pos.x = e.clientX - offset.x
  pos.y = e.clientY - offset.y
}

// 마우스 떼면 드래그 종료
function onMouseUp() {
  dragging = false
  document.body.style.userSelect = ''
}

onMounted(() => {
  window.addEventListener('mousemove', onMouseMove)
  window.addEventListener('mouseup', onMouseUp)
})
onUnmounted(() => {
  window.removeEventListener('mousemove', onMouseMove)
  window.removeEventListener('mouseup', onMouseUp)
})

function onSubmit() {
  if (!auth.isLoggined) {
    toast.info('로그인이 필요합니다.')
    router.push('/login')
    return
  }
  sendMessage()
}
</script>

<style scoped>
.chatbot {
  position: fixed;
  width: 400px;
  max-height: 400px;
  display: flex;
  flex-direction: column;
  border: 1px solid #ddd;
  padding: 8px;
  border-radius: 8px;
  background: white;
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

/* 드래그 핸들 영역 (선택적으로 스타일링) */
.drag-handle {
  cursor: move;
  padding: 4px 8px;
  font-size: 0.875rem;
  color: #666;
  text-align: center;
  border-bottom: 1px solid #eee;
  margin-bottom: 8px;
}

.messages {
  flex: 1;
  overflow-y: auto;
  margin-bottom: 8px;
}

.message {
  padding: 6px 10px;
  border-radius: 4px;
  margin-bottom: 6px;
  word-break: keep-all;
}

.message.user {
  background: #e0f7fa;
  text-align: right;
}

.message.bot {
  background: #f1f1f1;
  text-align: left;
}

.input-area {
  display: flex;
  gap: 8px;
}

input {
  flex: 1;
  padding: 8px;
  font-size: 1rem;
}

button {
  padding: 8px 12px;
  font-size: 1rem;
  cursor: pointer;
}
</style>
