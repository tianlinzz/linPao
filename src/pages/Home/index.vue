<script setup lang="ts">
import {onMounted, ref} from "vue";
import {getRecommendUser} from "@/services/user";
import UserList from "../../components/UserCard/index.vue";
import {UserInfo} from "@/types";

const recommendUserList = ref<UserInfo[]>([])

onMounted(async () => {
  const res = await getRecommendUser()
  recommendUserList.value = res.data
})

</script>

<template>
  <van-empty v-if="recommendUserList.length === 0" description="数据为空" />
  <template v-else :key="recommendUser.id" v-for="recommendUser in recommendUserList">
    <UserList :user="recommendUser" />
  </template>
</template>

<style scoped>

</style>