<script setup lang="ts">
import {onMounted, ref} from 'vue';
import {useRoute} from 'vue-router'
import {getFriendList} from '@/services/search'
import {UserInfo} from "@/types";
import {AxiosResponse} from "axios";
import { showToast } from "vant";
import UserCard from "@/components/UserCard/index.vue";


const route = useRoute()

const friendList = ref<UserInfo[]>([])

onMounted( () => {
  const { tags } = route.query;
  const tagNameList = JSON.parse(tags as string);
  getFriendList(tagNameList).then((res: AxiosResponse<UserInfo[]>) => {
    showToast('匹配成功');
    friendList.value = res.data
  })
});

</script>

<template>
  <van-empty v-if="friendList.length === 0" description="未匹配到相关的伙伴" />
  <template v-else :key="friend.id" v-for="friend in friendList">
    <UserCard :user="friend" />
  </template>
</template>

<style scoped>

</style>