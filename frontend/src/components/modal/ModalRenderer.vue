<!-- src/components/modal/ModalRenderer.vue -->
<template>
  <Teleport to="body">
    <component
      v-for="m in modals"
      :key="m.key"
      :is="m.component"
      v-bind="m.props"
      @resolve="handleResolve(m.key, $event)"
      @reject="handleReject(m.key, $event)"
      @close="handleClose(m.key)"
    />
  </Teleport>
</template>

<script setup lang="ts">
import { useModal } from '@/hooks/useModal'

// 훅에서 가져온 API
const { modals, resolveModal, closeModal } = useModal()

// 명시적으로 파라미터에 타입을 붙여 준 헬퍼 함수들
function handleResolve(key: string, payload: unknown): void {
  resolveModal(key, payload)
}

function handleReject(key: string, reason: unknown): void {
  closeModal(key, reason)
}

function handleClose(key: string): void {
  closeModal(key)
}
</script>
