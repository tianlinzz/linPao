<script setup lang="ts">
import { useRoute } from 'vue-router'
import { watchEffect } from 'vue'
import Layout from './components/Layout/index.vue'
import { useTitleStore } from './stores/title'

const titleStore = useTitleStore()
const { setTitle } = titleStore

const route = useRoute()

watchEffect(() => {
  if (route.meta.title) {
    setTitle(route.meta.title as string)
  } else {
    setTitle('linpao')
  }
  document.title = (route.meta.title || 'linpao') as string
})
</script>

<template>
  <Layout>
    <template #content>
      <router-view />
    </template>
  </Layout>
</template>
