<script setup lang="ts">
import { onMounted } from "vue";
import {useRouter} from 'vue-router'
import type {UserInfo} from '@/types'
import { getCurrentUser } from '@/services/search'
const router = useRouter()

onMounted(() => {
  getCurrentUser().then(res => {
    console.log(res)
  })
})

const userInfo: UserInfo = {
  id: 1,
  username: 'ztl',
  userAccount: 'tianlin08',
  avatarUrl: "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202106%2F07%2F20210607083404_14781.thumb.1000_0.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1688518854&t=03de0c28eac4034db7fb37fa00b0d58b",
  gender: 1,
  phone: '15072623487',
  email: '1343574516@qq.com',
  userCode: 1,
  tags: "[\"java\", \"javascript\", \"c++\"]"
}
const goEdit = (type: string, currentValue: string | number) => {
  router.push({
    path: '/center/edit',
    query: {
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
      <van-tag style="margin: 0 5px" v-for="tag in JSON.parse(userInfo.tags)" :key="tag" type="primary" round>{{ tag }}</van-tag>
      <van-icon name="arrow" :style="{ color: 'var(--van-cell-right-icon-color)' }"/>
    </template>
  </van-cell>
</template>

<style scoped>

</style>