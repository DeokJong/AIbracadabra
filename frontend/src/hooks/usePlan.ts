// src/hooks/usePlan.ts
import { defineStore } from 'pinia'
import type { FullDocument } from '@/hooks/useKakaoMap'
import axios from 'axios'
import { isAxiosError } from 'axios'
import { useToast } from 'vue-toastification'
import { ref } from 'vue'
import { CommonResponse } from '@/service/common'
import { startsWith } from 'lodash-es'

export type Plan = {
  title: string
  pno: number
  schedules: FullDocument[]
}

export const usePlan = defineStore('plan', () => {
  const toast = useToast()

  const myPlans = ref<Plan[]>([])

  const currentPlan = ref<Plan>({
    title: '',
    pno: 0,
    schedules: []
  })

  async function setupMyPlans () {
    try {
      const res = await axios.get<CommonResponse<Plan[]>>('/api/v1/plans/myplan')
      myPlans.value = res.data.data
    } catch (err) {
      if (isAxiosError(err)) {
        if (err.response?.status === 401) {
          toast.info("로그인이 필요합니다")
        } else if (startsWith(String(err.response?.status), '5')) {
          toast.warning("계획을 갖고오는 도중 문제가 발생했습니다.")
        }
      } else {
        toast.error("계획을 갖고 오는 도중 알 수 없는 문제가 생겼습니다.")
      }
    }
  }


  function setPlan(plan: Plan) {
    currentPlan.value = plan
  }

  function clearPlan() {
    currentPlan.value = { title: '', pno: 0, schedules: [] }
  }

  function appendSchedule(item: FullDocument) {
    currentPlan.value.schedules.push({ ...item })
  }

  async function removePlan() {
    if(!currentPlan.value.pno) {
    toast.warning("아직 저장하지 않은 계획입니다.")
    } else {
      await axios.delete(`/api/v1/plans/${currentPlan.value.pno}`)
      toast.info("여행 계획이 제거 되었습니다!")
      await setupMyPlans()
    }
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
    myPlans,
    setPlan,
    clearPlan,
    appendSchedule,
    savePlan,
    removeSchedule,
    setupMyPlans,
    removePlan
  }
})
