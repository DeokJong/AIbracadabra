<template>
  <!-- 챗봇 창 -->
  <div v-if="visible" class="chatbot" :style="chatbotStyle" @mousedown="onMouseDown">
    <!-- 드래그 핸들 -->

    <!-- 아이콘 바: 채팅 삭제 / 창 닫기 -->
    <div class="icon-bar">
      <div class="drag-handle">💬 드래그해서 이동</div>
      <div class="icon-actions">
        <v-tooltip text="채팅 기록 삭제" location="top" style="z-index: 1000;">
          <template #activator="{ props }">
            <v-icon v-bind="props" @click="clearChatHistory" class="clickable-icon">
              mdi-delete
            </v-icon>
          </template>
        </v-tooltip>

        <v-tooltip text="창 닫기" location="top" style="z-index: 1000;">
          <template #activator="{ props }">
            <v-icon v-bind="props" @click="visible = false" class="clickable-icon">
              mdi-close
            </v-icon>
          </template>
        </v-tooltip>
      </div>
    </div>

    <!-- 메시지 리스트 -->
    <div class="messages">
      <div v-for="(msg, idx) in chatHistory" :key="idx" :class="['message', msg.messageType]">
        <span v-if="msg.messageType === 'USER'">
          🧑 {{ auth.userInfo.name }}님: {{ msg.text }}
        </span>
        <span v-else>🤖 에브라: {{ msg.text }}</span>
      </div>
    </div>

    <!-- 입력 영역 -->
    <div class="input-area">
      <input v-model="inputText" @keyup.enter="onSubmit" placeholder="메시지를 입력하세요" :disabled="isChatLoading" />
      <button @click="onSubmit" :disabled="isChatLoading">전송</button>
    </div>
  </div>

  <!-- 챗봇 열기 버튼 -->
  <div v-else class="chatbot-open-btn">
    <v-tooltip text="봇 채팅 창 열기" location="top" style="z-index: 3000;">
      <template #activator="{ props }">
        <v-icon v-bind="props" @click="visible = true" class="clickable-icon open-icon">
          mdi-chat
        </v-icon>
      </template>
    </v-tooltip>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import useChatbot from '@/hooks/useChatBot'
import { useAuth } from '@/hooks/useAuth'
import { useToast } from 'vue-toastification'

const auth = useAuth()
const router = useRouter()
const toast = useToast()

const {
  inputText,
  chatHistory,
  isChatLoading,
  sendMessage,
  clearChatHistory,
  setupChatHistory
} = useChatbot()

// 드래그용 상태
const pos = reactive({
  x: window.innerWidth - 420,
  y: window.innerHeight - 440
})
let dragging = false
const offset = { x: 0, y: 0 }

// 챗봇 표시 여부
const visible = ref(true)

// 드래그 시작
function onMouseDown(e: MouseEvent) {
  dragging = true
  offset.x = e.clientX - pos.x
  offset.y = e.clientY - pos.y
  document.body.style.userSelect = 'none'
}

// 드래그 중
function onMouseMove(e: MouseEvent) {
  if (!dragging) return
  pos.x = e.clientX - offset.x
  pos.y = e.clientY - offset.y
}

// 드래그 종료
function onMouseUp() {
  dragging = false
  document.body.style.userSelect = ''
}

// 초기 설정
onMounted(() => {
  window.addEventListener('mousemove', onMouseMove)
  window.addEventListener('mouseup', onMouseUp)
  setupChatHistory()
})

// 정리
onUnmounted(() => {
  window.removeEventListener('mousemove', onMouseMove)
  window.removeEventListener('mouseup', onMouseUp)
})

// 전송 처리
function onSubmit() {
  if (!auth.isLoggined) {
    toast.info('로그인이 필요합니다.')
    router.push('/login')
    return
  }
  sendMessage()
}

// 스타일 바인딩 (reactive pos 반영)
const chatbotStyle = computed(() => ({
  top: pos.y + 'px',
  left: pos.x + 'px',
  zIndex: 1000
}))
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
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* 드래그 핸들 */
.drag-handle {
  cursor: move;
  padding: 4px 8px;
  font-size: 0.875rem;
  color: #666;
  text-align: left;
  border-bottom: 1px solid #eee;
  margin-bottom: 8px;
}

/* 아이콘 바 */
.icon-bar {
  display: flex;
  justify-content: space-between;
  /* ← 좌/우 끝으로 배치 */
  align-items: center;
  margin-bottom: 8px;
}

.icon-actions {
  display: flex;
  gap: 8px;
  /* 아이콘 사이 간격 */
}

.clickable-icon {
  cursor: pointer;
}

/* 메시지 리스트 */
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

.message.USER {
  background: #e0f7fa;
  text-align: right;
}

.message.ASSISTANT {
  background: #f1f1f1;
  text-align: left;
}

/* 입력 영역 */
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

/* 챗봇 열기 버튼 */
.chatbot-open-btn {
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 3000;
}

.open-icon {
  font-size: 32px;
  background: white;
  border-radius: 50%;
  padding: 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
}
</style>
