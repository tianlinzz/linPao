<script setup lang="ts">
import {ref} from "vue";
import {useRouter} from "vue-router";
import {GetTeamList} from "@/types";
import {useUserStore} from "@/stores/user";

const userStore = useUserStore()
const {userInfo} = userStore

const router = useRouter()

const teamQuery = ref<GetTeamList>({})
const isOnlyMe = ref<boolean>(false)

const goToResult = () => {
  router.push({
    path: '/center/team',
    query: isOnlyMe.value ? {
      ...teamQuery.value,
      userId: userInfo.id,
      isSearch: 1,
    } : {
      ...teamQuery.value,
      isSearch: 1,
    }
  })
}

</script>

<template>
  <van-search v-model="teamQuery.keyword" placeholder="请输入搜索关键词" />
  <div class="radio">
    <span>队伍状态：</span>
    <van-radio-group v-model="teamQuery.status" direction="horizontal">
      <van-radio :name="0">公开</van-radio>
      <van-radio :name="1">私人</van-radio>
      <van-radio :name="2">加密</van-radio>
    </van-radio-group>
  </div>
  <div class="radio">
    <span>是否仅查看自己创建的队伍：</span>
    <van-radio-group v-model="isOnlyMe" direction="horizontal">
      <van-radio :name="true">是</van-radio>
      <van-radio :name="false">否</van-radio>
    </van-radio-group>
  </div>
  <van-button class="create-button" @click="goToResult" round type="primary" size="large">查看结果</van-button>
</template>

<style scoped>
.radio {
  display: flex;
  justify-content: space-between;
  margin: 16px;
}
</style>