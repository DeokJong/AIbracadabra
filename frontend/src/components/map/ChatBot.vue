<template>
  <!-- 챗봇 창 -->
  <div v-if="visible" class="chatbot" :style="chatbotStyle" @mousedown="onMouseDown">
    <!-- 드래그 핸들 -->

    <!-- 아이콘 바: 채팅 삭제 / 창 닫기 -->
    <div class="icon-bar">
      <div class="drag-handle">
        <span class="drag-icon">🤖</span>
        <span class="drag-text">에브라 AI 어시스턴트</span>
      </div>
      <div class="icon-actions">
        <v-tooltip text="채팅 기록 삭제" location="top" style="z-index: 1000;">
          <template #activator="{ props }">
            <button v-bind="props" @click="clearChatHistory" class="action-btn delete-btn">
              <v-icon class="btn-icon">mdi-delete</v-icon>
            </button>
          </template>
        </v-tooltip>

        <v-tooltip text="창 닫기" location="top" style="z-index: 1000;">
          <template #activator="{ props }">
            <button v-bind="props" @click="visible = false" class="action-btn close-btn">
              <v-icon class="btn-icon">mdi-close</v-icon>
            </button>
          </template>
        </v-tooltip>
      </div>
    </div>

    <!-- 메시지 리스트 -->
    <div ref="messageContainer" class="messages">
      <div v-for="(msg, idx) in chatHistory" :key="idx" :class="['message', msg.messageType]">
        <div v-if="msg.messageType === 'USER'" class="user-message">
          <div class="message-content">
            <span class="message-text">{{ msg.text }}</span>
          </div>
          <div class="message-avatar user-avatar">
            <span>👤</span>
          </div>
        </div>
        <div v-else class="assistant-message">
          <div class="message-avatar assistant-avatar">
            <span>🤖</span>
          </div>
          <div class="message-content">
            <div class="assistant-name">에브라</div>
            <span class="message-text">{{ msg.text }}</span>
          </div>
        </div>
      </div>
      
      <!-- 로딩 인디케이터 -->
      <div v-if="isChatLoading" class="loading-message">
        <div class="message-avatar assistant-avatar">
          <span>🤖</span>
        </div>
        <div class="message-content">
          <div class="assistant-name">에브라</div>
          <div class="typing-indicator">
            <span></span>
            <span></span>
            <span></span>
          </div>
        </div>
      </div>
    </div>

    <!-- 입력 영역 -->
    <div class="input-area">
      <div class="input-container">
        <input 
          v-model="inputText" 
          @keyup.enter="sendMessage" 
          placeholder="메시지를 입력하세요..." 
          :disabled="isChatLoading"
          class="message-input"
        />
        <button 
          @click="sendMessage" 
          :disabled="isChatLoading || !inputText.trim()"
          class="send-btn"
        >
          <v-icon class="send-icon">mdi-send</v-icon>
        </button>
      </div>
    </div>
  </div>

  <!-- 챗봇 열기 버튼 -->
  <div v-else class="chatbot-open-btn">
    <v-tooltip text="AI 어시스턴트와 채팅하기" location="top" style="z-index: 3000;">
      <template #activator="{ props }">
        <button v-bind="props" @click="visible = true" class="open-btn">
          <v-icon class="open-icon">mdi-chat</v-icon>
          <div class="notification-dot"></div>
        </button>
      </template>
    </v-tooltip>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, computed, onMounted, onUnmounted, watch, nextTick } from 'vue'
import useChatbot from '@/hooks/useChatBot'

const {
  inputText,
  chatHistory,
  isChatLoading,
  sendMessage,
  clearChatHistory,
  setupChatHistory
} = useChatbot()

const messageContainer = ref<HTMLElement | null>(null)

watch(chatHistory, () => {
  nextTick(() => {
    if (messageContainer.value) {
      messageContainer.value.scrollTo({
        top: messageContainer.value.scrollHeight,
        behavior: 'smooth'
      })
    }
  })
}, { deep: true })

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

// 스타일 바인딩 (reactive pos 반영)
const chatbotStyle = computed(() => ({
  top: pos.y + 'px',
  left: pos.x + 'px',
  zIndex: 1000
}))
</script>

<style scoped>
/* 챗봇 메인 컨테이너 */
.chatbot {
  position: fixed;
  width: 400px;
  max-height: 600px;
  display: flex;
  flex-direction: column;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 24px;
  box-shadow: 
    0 20px 60px rgba(0, 0, 0, 0.15),
    0 0 0 1px rgba(255, 255, 255, 0.1) inset;
  overflow: hidden;
  transition: all 0.3s ease;
  animation: slideIn 0.4s ease-out;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: scale(0.9) translateY(20px);
  }
  to {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
}

/* 아이콘 바 (헤더) */
.icon-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 1.5rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  position: relative;
}

.icon-bar::before {
  content: '';
  position: absolute;
  inset: 0;
  background: url("data:image/svg+xml,%3Csvg width='20' height='20' viewBox='0 0 20 20' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='%23ffffff' fill-opacity='0.05'%3E%3Ccircle cx='10' cy='10' r='1'/%3E%3C/g%3E%3C/svg%3E");
}

.drag-handle {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  cursor: move;
  font-weight: 600;
  font-size: 1rem;
  position: relative;
  z-index: 2;
}

.drag-icon {
  font-size: 1.2rem;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.1); }
}

.drag-text {
  letter-spacing: 0.5px;
}

.icon-actions {
  display: flex;
  gap: 0.5rem;
  position: relative;
  z-index: 2;
}

.action-btn {
  width: 36px;
  height: 36px;
  border: none;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.action-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

.delete-btn:hover {
  background: rgba(239, 68, 68, 0.8);
}

.close-btn:hover {
  background: rgba(107, 114, 128, 0.8);
}

.btn-icon {
  font-size: 1.1rem;
}

/* 메시지 영역 */
.messages {
  flex: 1;
  overflow-y: auto;
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  background: linear-gradient(180deg, #f8f9fa 0%, #ffffff 100%);
  min-height: 300px;
  max-height: 400px;
}

.messages::-webkit-scrollbar {
  width: 6px;
}

.messages::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.05);
  border-radius: 3px;
}

.messages::-webkit-scrollbar-thumb {
  background: rgba(102, 126, 234, 0.3);
  border-radius: 3px;
  transition: background 0.3s ease;
}

.messages::-webkit-scrollbar-thumb:hover {
  background: rgba(102, 126, 234, 0.5);
}

/* 사용자 메시지 */
.user-message {
  display: flex;
  justify-content: flex-end;
  align-items: flex-end;
  gap: 0.75rem;
  animation: messageSlideIn 0.4s ease-out;
}

.message.USER .message-content {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 20px 20px 8px 20px;
  padding: 1rem 1.25rem;
  max-width: 70%;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
  position: relative;
}

.message.USER .message-content::after {
  content: '';
  position: absolute;
  bottom: -1px;
  right: -8px;
  width: 0;
  height: 0;
  border: 8px solid transparent;
  border-top: 8px solid #764ba2;
  border-right: none;
}

/* AI 어시스턴트 메시지 */
.assistant-message {
  display: flex;
  align-items: flex-start;
  gap: 0.75rem;
  animation: messageSlideIn 0.4s ease-out;
}

.message.ASSISTANT .message-content {
  background: white;
  border: 1px solid rgba(0, 0, 0, 0.08);
  border-radius: 20px 20px 20px 8px;
  padding: 1rem 1.25rem;
  max-width: 70%;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
  position: relative;
}

.message.ASSISTANT .message-content::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: -8px;
  width: 0;
  height: 0;
  border: 8px solid transparent;
  border-top: 8px solid white;
  border-left: none;
}

.assistant-name {
  font-size: 0.75rem;
  font-weight: 600;
  color: #667eea;
  margin-bottom: 0.5rem;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.message-text {
  line-height: 1.5;
  word-break: break-word;
  display: block;
}

.message-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1rem;
  flex-shrink: 0;
  border: 2px solid rgba(255, 255, 255, 0.9);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.user-avatar {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
}

.assistant-avatar {
  background: linear-gradient(135deg, #f093fb, #f5576c);
  color: white;
}

/* 로딩 메시지 */
.loading-message {
  display: flex;
  align-items: flex-start;
  gap: 0.75rem;
  animation: messageSlideIn 0.4s ease-out;
}

.loading-message .message-content {
  background: white;
  border: 1px solid rgba(0, 0, 0, 0.08);
  border-radius: 20px 20px 20px 8px;
  padding: 1rem 1.25rem;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
}

.typing-indicator {
  display: flex;
  gap: 4px;
  align-items: center;
}

.typing-indicator span {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #667eea;
  animation: typing 1.4s ease-in-out infinite;
}

.typing-indicator span:nth-child(1) { animation-delay: 0s; }
.typing-indicator span:nth-child(2) { animation-delay: 0.2s; }
.typing-indicator span:nth-child(3) { animation-delay: 0.4s; }

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
    opacity: 0.4;
  }
  30% {
    transform: translateY(-10px);
    opacity: 1;
  }
}

@keyframes messageSlideIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 입력 영역 */
.input-area {
  padding: 1.5rem;
  background: white;
  border-top: 1px solid rgba(0, 0, 0, 0.05);
}

.input-container {
  display: flex;
  gap: 0.75rem;
  align-items: center;
  background: #f8f9fa;
  border-radius: 25px;
  padding: 0.5rem;
  border: 1px solid rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.input-container:focus-within {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.message-input {
  flex: 1;
  border: none;
  background: transparent;
  padding: 0.75rem 1rem;
  font-size: 1rem;
  color: #333;
  outline: none;
  font-family: inherit;
}

.message-input::placeholder {
  color: #999;
}

.message-input:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.send-btn {
  width: 40px;
  height: 40px;
  border: none;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
}

.send-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.send-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none;
}

.send-icon {
  font-size: 1.1rem;
}

/* 챗봇 열기 버튼 */
.chatbot-open-btn {
  position: fixed;
  bottom: 30px;
  right: 30px;
  z-index: 3000;
}

.open-btn {
  width: 64px;
  height: 64px;
  border: none;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  box-shadow: 
    0 8px 25px rgba(102, 126, 234, 0.4),
    0 0 0 0 rgba(102, 126, 234, 0.5);
  position: relative;
  animation: float 3s ease-in-out infinite;
}

.open-btn:hover {
  transform: translateY(-3px) scale(1.05);
  box-shadow: 
    0 12px 35px rgba(102, 126, 234, 0.5),
    0 0 0 8px rgba(102, 126, 234, 0.1);
}

.open-icon {
  font-size: 1.8rem;
}

.notification-dot {
  position: absolute;
  top: 8px;
  right: 8px;
  width: 12px;
  height: 12px;
  background: #f56565;
  border-radius: 50%;
  border: 2px solid white;
  animation: pulse-dot 2s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-8px); }
}

@keyframes pulse-dot {
  0%, 100% { transform: scale(1); opacity: 1; }
  50% { transform: scale(1.2); opacity: 0.8; }
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .chatbot {
    width: 95vw;
    max-width: 380px;
    max-height: 80vh;
  }
  
  .icon-bar {
    padding: 1rem;
  }
  
  .drag-text {
    font-size: 0.9rem;
  }
  
  .messages {
    padding: 1rem;
    min-height: 250px;
  }
  
  .input-area {
    padding: 1rem;
  }
  
  .open-btn {
    width: 56px;
    height: 56px;
  }
  
  .open-icon {
    font-size: 1.6rem;
  }
  
  .chatbot-open-btn {
    bottom: 20px;
    right: 20px;
  }
}

@media (max-width: 480px) {
  .message.USER .message-content,
  .message.ASSISTANT .message-content {
    max-width: 85%;
  }
  
  .message-input {
    font-size: 16px; /* iOS zoom 방지 */
  }
}
</style>
