<script setup lang="ts">
import type {UserInfo} from '../../types'

const userInfo: UserInfo = {
  id: 1,
  username: 'ztl',
  userAccount: 'tianlin08',
  avatarUrl: "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202106%2F07%2F20210607083404_14781.thumb.1000_0.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1688518854&t=03de0c28eac4034db7fb37fa00b0d58b",
  gender: 1,
  phone: '15072623487',
  email: '1343574516@qq.com',
  userCode: 1,
  tag: ["java", "javascript", "c++"]
}
// TODO 添加修改用户信息的逻辑

</script>

<template>
  <van-cell title="昵称" is-link to="/center/edit" :value="userInfo.username" center/>
  <van-cell title="账号" :value="userInfo.userAccount" center/>
  <van-cell title="头像" is-link to="/center/edit" center>
    <img :src="userInfo.avatarUrl" alt="avatar" style="width: 50px; height: 50px; border-radius: 50%;"/>
  </van-cell>
  <van-cell title="性别" is-link to="/center/edit" :value="userInfo.gender ? '女': '男' " center/>
  <van-cell title="手机号" is-link to="/center/edit" :value="userInfo.phone" center/>
  <van-cell title="邮箱" is-link to="/center/edit" :value="userInfo.email" center/>
  <van-cell title="用户编号" :value="userInfo.userCode" center/>
  <van-cell title="标签" is-link to="/center/edit" center>
    <template #right-icon>
      <van-tag style="margin: 0 5px" v-for="tag in userInfo.tag" :key="tag" type="primary" round>{{ tag }}</van-tag>
      <van-icon name="arrow" :style="{ color: 'var(--van-cell-right-icon-color)' }"/>
    </template>
  </van-cell>
</template>

<style scoped>

</style>