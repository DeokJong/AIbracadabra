<template>
  <v-text-field
    :label="labelName"
    :type="showPassword ? 'text' : 'password'"
    outlined
    dense
    class="ma-4"
    :rules="rules"
    prepend-inner-icon="mdi-lock-check"
    :append-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
    @click:append="showPassword = !showPassword"
    :model-value="modelValue"
    @update:modelValue="onUpdate"
    :required="isRequired"
  />
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useValidationRules } from '@/hooks/useValidationRules'

const props = defineProps<{
  labelName: string
  modelValue: string | undefined
  isRequired: boolean
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: string): void
}>()

const showPassword = ref(false)
const { passwordRules } = useValidationRules()

const onUpdate = (value: string) => {
  emit('update:modelValue', value)
}

const rules = computed(() => {
  if (!props.isRequired && (props.modelValue === undefined || props.modelValue.length === 0)  ) return []
  return [
    (v: string) => !!v || '비밀번호를 입력하세요.',
    ...passwordRules,
  ]
})


</script>
