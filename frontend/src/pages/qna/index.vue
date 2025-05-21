<template>
  <ItemTable
    :apiCallUrl="apiCallUrl"
  />
</template>

<script setup lang="ts">
import ItemTable from '@/components/board/ItemTable.vue';
import { buildQueryString } from '@/util/queryParams';
import { computed } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();

const API_URL = '/api/v1/qna'
const QUERY_STRING = buildQueryString(route.query)

const apiCallUrl = computed(() => `${API_URL}${QUERY_STRING ? '?' + QUERY_STRING : ''}`)

</script>
