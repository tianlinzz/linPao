<script setup lang="ts">
import {UserInfo} from "@/types";

interface Props {
  user: UserInfo
}
defineProps<Props>()
</script>

<template>
  <van-card
      :title="user.username"
      :thumb="user.avatarUrl"
  >
    <template #tags>
      <van-tag
          v-for="tag in JSON.parse(user.tags)"
          :key="user.id"
          type="primary"
          :style="{ margin: '4px' }"
      >
        {{tag}}
      </van-tag>
    </template>
    <template #footer>
      <van-button size="mini" type="primary">查看联系方式</van-button>
    </template>
  </van-card>
</template>

<style scoped>

</style>