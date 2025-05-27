// src/hooks/useChatBot.ts
import { useAuth } from '@/hooks/useAuth'
import { usePlan, type Plan } from '@/hooks/usePlan'
import router from '@/router'
import { CommonResponse } from '@/service/common'
import axios from 'axios'
import { ref } from 'vue'
import { useToast } from 'vue-toastification'

type ChatResponse = {
  message: string
  recommendPlan: Plan
}

export type ChatMessage = {
  messageType: 'USER' | 'ASSISTANT' | 'SYSTEM' | 'TOOL'
  text: string
}

const useChatbot = () => {
  const auth = useAuth()
  const toast = useToast()
  const inputText = ref<string>('')
  const isChatLoading = ref<boolean>(false)
  const chatHistory = ref<ChatMessage[]>([])

  const planStore = usePlan()

  const clearChatHistory = async () => {
    if (!auth.isLoggined) {
      toast.info('로그인이 필요합니다.')
      router.push('/login')
      return
    }
    axios
      .post('/api/v1/chat/cleanChatMemory')
      .then(() => {
        chatHistory.value = []
      })
      .catch(() => {
        toast.warning('메세지 기록을 제거하는데 실패하였습니다!')
      })
  }

  const setupChatHistory = async () => {
    if (!auth.isLoggined) {
      return
    }

    axios
      .get<CommonResponse<ChatMessage[]>>('/api/v1/chat/chatHistory')
      .then((res) => {
        res.data.data.forEach((e) => {
          if (e.messageType === 'USER') {
            chatHistory.value.push({
              messageType: e.messageType,
              text: e.text,
            })
          } else {
            chatHistory.value.push({
              messageType: e.messageType,
              text: (JSON.parse(e.text) as ChatResponse).message,
            })
          }
        })
      })
      .catch(() => {
        toast.warning('이전 채팅 내역을 불러오는데 실패하였습니다.')
      })
  }

  // 메시지 전송 함수
  async function sendMessage() {
    if (!auth.isLoggined) {
      toast.info('로그인이 필요합니다.')
      router.push('/login')
      return
    }
    const text = inputText.value.trim()
    if (!text) return

    // 1) 사용자 메시지 등록
    chatHistory.value.push({
      messageType: 'USER',
      text,
    })
    inputText.value = ''

    try {
      isChatLoading.value = true
      const { data } = await axios.get<CommonResponse<ChatResponse>>(`/api/v1/chat/recommend`, {
        params: { userInput: text },
      })
      // 4) 실제 응답으로 대체
      chatHistory.value.push({
        messageType:"ASSISTANT",
        text:data.data.message})

      const recommendPlan = data.data.recommendPlan
      if (recommendPlan != null &&recommendPlan.schedules.length) {
        console.log('추천 일정:', recommendPlan)
        recommendPlan.pno=0
        planStore.setPlan(recommendPlan)
      }
    } catch {
            chatHistory.value.push({
        messageType:"ASSISTANT",
        text:'⚠️ 서버 요청 중 오류가 발생했습니다.'})
    } finally {
      isChatLoading.value = false
    }
  }

  return {
    inputText,
    chatHistory,
    isChatLoading,
    clearChatHistory,
    sendMessage,
    setupChatHistory,
  }
}

export default useChatbot
