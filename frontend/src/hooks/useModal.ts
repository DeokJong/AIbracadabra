// ./src/hooks/useModal
import { ref, markRaw, type  Component } from 'vue'

interface ModalInfo<T = any> {
  key: string
  component: Component
  props: Record<string, any>
  resolve: (value: T) => void
  reject: (reason?: any) => void
}

const modals = ref<ModalInfo[]>([])

const addModal = <T = any>(
  component: Component,
  props: Record<string, any> = {}
): Promise<T> =>
  new Promise<T>((resolve, reject) => {
    const key = `modal_${Date.now()}_${Math.random()}`
    modals.value.push({
      key,
      component: markRaw(component),
      props,
      resolve,
      reject,
    })
  })

const resolveModal = <T = any>(key: string, payload: T): void => {
  const idx = modals.value.findIndex((m) => m.key === key)
  if (idx !== -1) {
    modals.value[idx].resolve(payload)
    modals.value.splice(idx, 1)
  }
}

const closeModal = (key: string, reason?: any): void => {
  const idx = modals.value.findIndex((m) => m.key === key)
  if (idx !== -1) {
    modals.value[idx].reject(reason)
    modals.value.splice(idx, 1)
  }
}

export const useModal = () => ({
  modals,
  addModal,
  resolveModal,
  closeModal,
})
