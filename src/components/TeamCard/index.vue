<script setup lang="ts">
import {useUserStore} from "@/stores/user";
import {TeamInfo} from "@/types";

const userStore = useUserStore()
const {userInfo} = userStore

interface Props {
  team: TeamInfo
}

defineProps<Props>()
</script>

<template>
  <van-card
      :title="team.name"
      :thumb="userInfo.avatarUrl"
  >
    <template #tags>
      <van-tag
          type="primary"
      >
        {{ team.status }}
      </van-tag>
    </template>
    <template #footer>
      <van-button size="mini" type="primary">查看联系方式</van-button>
    </template>
  </van-card>
</template>

<style scoped>

</style>