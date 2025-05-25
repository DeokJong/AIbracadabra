import { usePlan, type Plan } from '@/hooks/usePlan'
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
  const toast = useToast()
  const inputText = ref<string>('')
  const isChatLoading = ref<boolean>(false)
  const chatHistory = ref<ChatMessage[]>([])

  const { currentPlan } = usePlan()

  const clearChatHistory = async () => {
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
    const text = inputText.value.trim()
    if (!text) return

    // 1) 사용자 메시지 등록
    chatHistory.value.push({
      messageType: 'USER',
      text,
    })
    inputText.value = ''

    // 2) ASSISTANT placeholder 등록
    chatHistory.value.push({ messageType: 'ASSISTANT', text: '응답중.' })
    const placeholderIndex = chatHistory.value.length - 1

    // 3) 점 개수 순환 애니메이션
    let dotCount = 1
    const intervalId = setInterval(() => {
      dotCount = (dotCount % 3) + 1 // 1 → 2 → 3 → 1 ...
      chatHistory.value[placeholderIndex].text = '응답중' + '.'.repeat(dotCount)
    }, 500) // 0.5초마다 변경

    try {
      isChatLoading.value = true
      const { data } = await axios.get<CommonResponse<ChatResponse>>(`/api/v1/chat/recommend`, {
        params: { userInput: text },
      })
      // 4) 실제 응답으로 대체
      chatHistory.value[placeholderIndex].text = data.data.message

      const recommendPlan =data.data.recommendPlan
      if(recommendPlan) {
        currentPlan.title = recommendPlan.title
        currentPlan.schedules = recommendPlan.schedules
      }

    } catch (error) {
      chatHistory.value[placeholderIndex].text = '⚠️ 서버 요청 중 오류가 발생했습니다.'
    } finally {
      isChatLoading.value = false
      clearInterval(intervalId)
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
