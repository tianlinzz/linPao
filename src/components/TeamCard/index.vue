<script setup lang="ts">
import {computed, ref} from "vue";
import {useRouter} from "vue-router";
import {useUserStore} from "@/stores/user";
import {JoinTeam, TeamInfo} from "@/types";

interface Props {
  team: TeamInfo
}

interface Emits {
  (event: 'joinTeam', joinTeam: JoinTeam): void,
  (event: 'dissolveTeam', teamId: number): void,
  (event: 'quitTeam', teamId: number): void,
}

const router = useRouter()

const userStore = useUserStore()
const {userInfo} = userStore

const props = defineProps<Props>()
const emits = defineEmits<Emits>()

const showDialog = ref<boolean>(false);
const password = ref<string>('')

const teamUserNum = computed<number>(() => {
  const {team: {userList = []}} = props
  return userList.length + 1;
})

const isExist = computed<boolean>(() => {
  const {team: {userList = []}} = props
  return userList.some(item => item.id === userInfo.id)
})

const statusMap = {
  0: '公开',
  1: '私密',
  2: '加密',
}

const update = (id: number) => {
  router.push({
    path: '/center/createOrUpdate',
    query: {
      id,
    }
  })
}

let joinTeam: JoinTeam = {
  teamId: 0,
  password: '',
}
const onConfirm = () => {
  joinTeam.password = password.value
  emits('joinTeam', joinTeam)
}

const join = (teamId: number, status: number) => {
  joinTeam.teamId = teamId
  if (Number(status) === 2) {
    showDialog.value = true
    return
  }
  emits('joinTeam', joinTeam)
}

const dissolve = (teamId: number) => {
  emits('dissolveTeam', teamId)
}

const quit = (teamId: number) => {
  emits('quitTeam', teamId)
}

</script>

<template>
  <van-card
      :title="team.name"
      :desc="team.description"
      :thumb="team.createUser?.avatarUrl"
      :tag="statusMap[team.status]"
  >
    <template #num>
      <span style="color: cornflowerblue;font-size: 16px">{{ teamUserNum }}/{{ team.maxNum }}</span>
    </template>
    <template v-if="team.userId === userInfo.id" #price-top>
      <van-tag type="primary">
        我的队伍
      </van-tag>
    </template>
    <template #footer>
      <van-button @click="join(team.id, team.status)" v-if="team.userId !== userInfo.id && Number(team.status) !== 1 && !isExist" size="mini" type="primary">加入队伍</van-button>
      <van-button @click="update(team.id)" v-if="team.userId === userInfo.id" size="mini" type="primary">修改队伍
      </van-button>
      <van-button @click="quit(team.id)" v-if="isExist || (team.userId === userInfo.id && team.userList?.length !== 0)" size="mini" type="danger">退出队伍
      </van-button>
      <van-button @click="dissolve(team.id)" v-if="team.userId === userInfo.id" size="mini" type="danger">解散队伍
      </van-button>
    </template>
  </van-card>
  <van-dialog
      v-if="Number(team.status) === 2"
      v-model:show="showDialog"
      title="请输入队伍密码"
      :show-cancel-button="false"
      @confirm="onConfirm"
  >
    <van-field
        v-model="password"
        name="password"
        type="password"
        label="队伍密码"
        placeholder="请输入队伍密码"
    />
  </van-dialog>
</template>

<style scoped>

</style>