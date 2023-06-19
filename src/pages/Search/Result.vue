<script setup lang="ts">
import {onMounted, ref} from 'vue';
import {useRoute} from 'vue-router'
import {getFriendList} from '@/services/search'
import {UserInfo} from "@/types";
import {AxiosResponse} from "axios";


const route = useRoute()

const friendList = ref<UserInfo[]>([])

onMounted( () => {
  const { tags } = route.query;
  const tagNameList = JSON.parse(tags as string);
  getFriendList(tagNameList).then((res: AxiosResponse<UserInfo[]>) => {
    friendList.value = res.data
  })
});

</script>

<template>
  <template :key="friend.id" v-for="friend in friendList">
    <van-card
        :title="friend.username"
        :thumb="friend.avatarUrl"
    >
      <template #tags>
        <van-tag
            v-for="tag in JSON.parse(friend.tags)"
            :key="friend.id"
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
</template>

<style scoped>

</style>