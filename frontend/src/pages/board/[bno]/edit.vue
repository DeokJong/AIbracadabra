<template>
  fds
<ModifyDetailBoard :bno="bno" />

</template>
<script setup lang="ts">
import { useRoute } from 'vue-router'
import ModifyDetailBoard from '@/components/board/ModifyDetailBoard.vue'

const route = useRoute()
const bno = (route.params as { bno: string }).bno

</script>