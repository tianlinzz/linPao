<script setup lang="ts">
import {ref, onMounted} from 'vue'
import {getTeamList} from "@/services/team";
import {GetTeamList, TeamInfo} from "@/types";

const teamList = ref<TeamInfo[]>([])

onMounted(() => {
  getTeamListData()
})

const getTeamListData = async (params?: GetTeamList) => {
  const res = await getTeamList(params)
  console.log(res)
  teamList.value = res.data as TeamInfo[]
}

</script>

<template>
  <van-button class="create-button" round to="/center/create" type="primary" size="large">创建队伍</van-button>
</template>

<style scoped>
.create-button {
  margin-top: 16px;
}
</style>