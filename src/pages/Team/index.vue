<script setup lang="ts">
import {ref, onMounted} from 'vue'
import {useRoute} from "vue-router";
import {dissolveTeam, getTeamList, joinTeamAPI, quitTeamAPI} from "@/services/team";
import {GetTeamList, TeamInfo, JoinTeam} from "@/types";
import TeamCard from "@/components/TeamCard/index.vue";
import {showConfirmDialog, showToast} from "vant";

const route = useRoute()
const query = route.query

const teamList = ref<TeamInfo[]>([])

onMounted(() => {
  const {isSearch, ...params} = query
  query.isSearch ? getTeamListData(params) : getTeamListData()
})

const getTeamListData = async (params?: GetTeamList) => {
  const res = await getTeamList(params)
  teamList.value = res.data as TeamInfo[]
}

const join = async (joinTeam: JoinTeam) => {
  const res = await joinTeamAPI(joinTeam)
  if (res.code === 200) {
    await getTeamListData()
    showToast({
      type: 'success',
      message: '加入成功',
    })
  }
}

const dissolve = async (teamId : number) => {
  showConfirmDialog({
    title: '提示',
    message: '解散队伍后，队伍将无法恢复，队伍内的成员将自动退出队伍，是否确认解散队伍？',
  }).then(async () => {
    const res = await dissolveTeam(teamId)
    if (res.code === 200) {
      await getTeamListData()
      showToast({
        type: 'success',
        message: '解散成功',
      })
    }
  })
}

const quit = (teamId: number) => {
  showConfirmDialog({
    title: '提示',
    message: '退出队伍后，需要重新加入队伍，是否确认退出队伍？',
  }).then(async () => {
    const res = await quitTeamAPI(teamId)
    if (res.code === 200) {
      await getTeamListData()
      showToast({
        type: 'success',
        message: '退出成功',
      })
    }
  })
}

const reset = () => {
  window.location.href = '/center/team'
  getTeamListData()
}

</script>

<template>
  <div class="search">
    <van-button to="/center/teamSearch" type="primary" size="large">搜索队伍</van-button>
    <van-button class="reset-button" @click="reset" size="large">重置</van-button>
  </div>
  <van-button class="create-button" round to="/center/createOrUpdate" type="primary" size="large">创建队伍</van-button>
  <template v-if="teamList.length !== 0" v-for="team in teamList">
    <TeamCard @dissolveTeam="dissolve" @joinTeam="join" @quitTeam="quit" :team="team"/>
  </template>
  <van-empty v-else description="暂时未搜索到队伍" />
</template>

<style scoped>
.search {
  display: flex;
  justify-content: space-between;
  margin: 16px 0;
}
.reset-button {
  margin-left: 10px;
}
.create-button {
  margin-bottom: 16px;
}
</style>