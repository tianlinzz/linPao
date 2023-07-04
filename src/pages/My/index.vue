<script setup lang="ts">
import {computed} from "vue";
import {useRouter} from 'vue-router'
import {useUserStore} from '@/stores/user'
import dayjs from "dayjs";

const userStore = useUserStore()
const {userInfo} = userStore

const router = useRouter()


const goEdit = (type: string, currentValue: string | number | string[]) => {
  router.push({
    path: '/center/edit',
    query: {
      id: userInfo.value.id,
      type,
      currentValue
    }
  })
}

const tagList = computed<string[]>(() => {
  // 过滤重复的标签，或者空标签
  return Array.from(new Set(userInfo.value.tags)).map((tag: any) => {
    if (tag) {
      return tag
    }
  })
})

</script>

<template>
  <template v-if="userInfo.id">
    <van-cell title="昵称" @click="goEdit('username', userInfo.username)" is-link to="/center/edit"
              :value="userInfo.username" center/>
    <van-cell title="账号" :value="userInfo.userAccount" center/>
    <van-cell title="头像" @click="goEdit('avatarUrl', userInfo.avatarUrl)" is-link to="/center/edit" center>
      <img :src="userInfo.avatarUrl" alt="avatar" style="width: 50px; height: 50px; border-radius: 50%;"/>
    </van-cell>
    <van-cell title="性别" @click="goEdit('gender', userInfo.gender)" is-link to="/center/edit"
              :value="userInfo.gender ? '女': '男' " center/>
    <van-cell title="手机号" @click="goEdit('phone', userInfo.phone)" is-link to="/center/edit" :value="userInfo.phone"
              center/>
    <van-cell title="邮箱" @click="goEdit('email', userInfo.email)" is-link to="/center/edit" :value="userInfo.email"
              center/>
    <van-cell title="用户编号" :value="userInfo.userCode" center/>
    <van-cell title="标签" @click="goEdit('tags', tagList)" is-link to="/center/edit" center>
      <template #right-icon>
        <van-tag style="margin: 5px" v-for="tag in tagList" :key="tag" type="primary" round>{{ tag }}</van-tag>
        <van-icon name="arrow" :style="{ color: 'var(--van-cell-right-icon-color)' }"/>
      </template>
    </van-cell>
    <van-cell title="注册时间" :value="dayjs(userInfo.createTime).format('YYYY-MM-DD HH:mm:ss')" center/>
  </template>
  <van-empty v-else description="用户未登录" />
</template>

<style scoped>
</style>