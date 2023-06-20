<script setup lang="ts">
import { onMounted, ref } from "vue";
import {useRouter} from 'vue-router'
import type {UserInfo} from '@/types'
import { getCurrentUser } from '@/services/search'
const router = useRouter()

const userInfo = ref<UserInfo>({})

onMounted(async () => {
  const res = await getCurrentUser()
  res.data.tags = JSON.parse(res.data.tags) // 序列化标签
  userInfo.value = res.data
})



const goEdit = (type: string, currentValue: string | number) => {
  router.push({
    path: '/center/edit',
    query: {
      id: userInfo.value.id,
      type,
      currentValue
    }
  })
}
</script>

<template>
  <van-cell title="昵称" @click="goEdit('username', userInfo.username)" is-link to="/center/edit" :value="userInfo.username" center/>
  <van-cell title="账号" :value="userInfo.userAccount" center/>
  <van-cell title="头像" @click="goEdit('avatarUrl', userInfo.avatarUrl)" is-link to="/center/edit" center>
    <img :src="userInfo.avatarUrl" alt="avatar" style="width: 50px; height: 50px; border-radius: 50%;"/>
  </van-cell>
  <van-cell title="性别" @click="goEdit('gender', userInfo.gender)" is-link to="/center/edit" :value="userInfo.gender ? '女': '男' " center/>
  <van-cell title="手机号" @click="goEdit('phone', userInfo.phone)" is-link to="/center/edit" :value="userInfo.phone" center/>
  <van-cell title="邮箱" @click="goEdit('email', userInfo.email)" is-link to="/center/edit" :value="userInfo.email" center/>
  <van-cell title="用户编号" :value="userInfo.userCode" center/>
  <van-cell title="标签" @click="goEdit('tags', userInfo.tags)" is-link to="/center/edit" center>
    <template #right-icon>
      <van-tag style="margin: 0 5px" v-for="tag in userInfo?.tags" :key="tag" type="primary" round>{{ tag }}</van-tag>
      <van-icon name="arrow" :style="{ color: 'var(--van-cell-right-icon-color)' }"/>
    </template>
  </van-cell>
</template>

<style scoped>

</style>