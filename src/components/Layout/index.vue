<script setup lang="ts">
import {showToast} from 'vant';
import {ref} from "vue";
import {useTitleStore} from '../../stores/title'
import {storeToRefs} from 'pinia'

const titleStore = useTitleStore()

const {title} = storeToRefs(titleStore)
const onClickLeft = () => history.back();
const onClickRight = () => showToast('按钮');
const active = ref<string>("home");
const onChange = (index: number) => showToast(`进入${index}的页面`);
</script>

<template>
  <van-nav-bar
      :title="title"
      left-arrow
      :route="true"
      @click-left="onClickLeft"
      @click-right="onClickRight"
  >
    <template #right>
      <van-icon name="search" size="18"/>
    </template>
  </van-nav-bar>
  <router-view/>
  <van-tabbar v-model="active" @change="onChange">
    <van-tabbar-item icon="home-o" name="home" to="/center/home">主页</van-tabbar-item>
    <van-tabbar-item icon="friends-o" name="team" to="/center/team">队伍</van-tabbar-item>
    <van-tabbar-item icon="user-circle-o" name="my" to="/center/my">我的</van-tabbar-item>
  </van-tabbar>
</template>

<style scoped>

</style>