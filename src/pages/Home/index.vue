<script setup lang="ts">
import {onMounted, ref} from "vue";
import {getRecommendUser} from "@/services/user";
import UserCard from "@/components/UserCard/index.vue";
import {UserInfo} from "@/types";

const recommendUserList = ref<UserInfo[]>([])

onMounted(async () => {
  const res = await getRecommendUser(10,1)
  recommendUserList.value = res.data?.items || [];
})

</script>

<template>
  <van-empty v-if="recommendUserList?.length === 0" description="数据为空" />
  <template v-else :key="recommendUser?.id" v-for="recommendUser in recommendUserList">
    <UserCard :user="recommendUser" />
  </template>
</template>

<style scoped>

</style>