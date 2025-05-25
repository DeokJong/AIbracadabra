// src/hooks/plan.ts
import { defineStore } from 'pinia'
import type { FullDocument } from '@/hooks/useKakaoMap'
import axios from 'axios'
import { isAxiosError } from 'axios'
import { useToast } from 'vue-toastification'
import { ref } from 'vue'

export type Plan = {
  title: string
  pno: number
  schedules: FullDocument[]
}

export const usePlan = defineStore('plan', () => {
  const toast = useToast()

  const currentPlan = ref<Plan>({
    title: '',
    pno: 0,
    schedules: []
  })

  function setPlan(plan: Plan) {
    currentPlan.value = plan
  }

  function clearPlan() {
    currentPlan.value = { title: '', pno: 0, schedules: [] }
  }

  function appendSchedule(item: FullDocument) {
    currentPlan.value.schedules.push({ ...item })
  }

  async function savePlan() {
    if(!currentPlan.value.title) {
      toast.warning("일정 제목은 필수 입니다!")
      return ;
    }
    try {
      if (currentPlan.value.pno === 0) {
        const res = await axios.post('/api/v1/plans', currentPlan.value)
        currentPlan.value.pno = res.data.data.pno
      } else {
        await axios.put(`/api/v1/plans/${currentPlan.value.pno}`, currentPlan.value)
      }
      toast.info('여행 계획이 저장되었습니다.')
    } catch (err) {
      if (isAxiosError(err) && err.response?.status === 401) {
        toast.info('로그인 후 이용하세요')
      } else {
        toast.warning('저장 중 오류가 발생했습니다.')
      }
    }
  }

  function removeSchedule(index: number) {
    currentPlan.value.schedules.splice(index, 1)
  }

  return {
    currentPlan,
    setPlan,
    clearPlan,
    appendSchedule,
    savePlan,
    removeSchedule
  }
})
