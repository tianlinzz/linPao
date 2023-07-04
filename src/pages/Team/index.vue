<script setup lang="ts">
import {ref, onMounted} from 'vue'
import {getTeamList} from "@/services/team";
import {GetTeamList, TeamInfo} from "@/types";
import TeamCard from "@/components/TeamCard/index.vue";

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
  <template v-if="teamList.length !== 0" v-for="team in teamList">
    <TeamCard :team="team"/>
  </template>
  <van-empty v-else description="暂时未搜索到队伍" />
</template>

<style scoped>
.create-button {
  margin: 16px 0;
}
</style>