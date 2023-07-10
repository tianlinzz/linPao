<script setup lang="ts">
import {useRoute, useRouter} from 'vue-router'
import {watchEffect} from 'vue'
import {useTitleStore} from './stores/title'

const titleStore = useTitleStore()
const {setTitle, setActive, setShowTabbar} = titleStore

const route = useRoute()
const router = useRouter()


watchEffect(() => {
  setTitle(route.meta.title || 'linpao' as string)
  if (route.meta.show) {
    setActive(route.name as string)
  }
  setShowTabbar(route.meta.show as boolean)
  document.title = (route.meta.title || 'linpao') as string
})
</script>

<template>
  <router-view/>
</template>
