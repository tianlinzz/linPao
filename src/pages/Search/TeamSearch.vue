<script setup lang="ts">
import {ref} from "vue";
import {useRouter} from "vue-router";
import {GetTeamList} from "@/types";

const router = useRouter()

const teamQuery = ref<GetTeamList>({
  isOnlyJoin: false,
  isOnlyCreate: false,
  isOnlyNotJoin: false,
  statusList: [0, 1, 2],
})

const goToResult = () => {
  const query = {
    ...teamQuery.value,
    isSearch: 1,
  }
  router.push({
    path: '/center/team',
    // @ts-ignore
    query
  })
}

</script>

<template>
  <van-search v-model="teamQuery.keyword" placeholder="请输入搜索关键词"/>
  <div class="radio">
    <span>队伍状态：</span>
    <van-checkbox-group v-model="teamQuery.statusList" direction="horizontal">
      <van-checkbox :name="0">公开</van-checkbox>
      <van-checkbox :name="1">私密</van-checkbox>
      <van-checkbox :name="2">加密</van-checkbox>
    </van-checkbox-group>
  </div>
  <div class="radio">
    <span>仅查看自己创建的队伍：</span>
    <van-radio-group v-model="teamQuery.isOnlyCreate" direction="horizontal">
      <van-radio :name="true">是</van-radio>
      <van-radio :name="false">否</van-radio>
    </van-radio-group>
  </div>
  <div class="radio">
    <span>仅查看自己加入的队伍：</span>
    <van-radio-group v-model="teamQuery.isOnlyJoin" direction="horizontal">
      <van-radio :name="true">是</van-radio>
      <van-radio :name="false">否</van-radio>
    </van-radio-group>
  </div>
  <div class="radio">
    <span>仅查看自己未加入的队伍：</span>
    <van-radio-group v-model="teamQuery.isOnlyNotJoin" direction="horizontal">
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