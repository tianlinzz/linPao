<script setup lang="ts">
import {ref} from "vue";
import { useRouter, useRoute } from 'vue-router'
import {UserLogin} from "@/types";
import { login } from "@/services/user";
import { showToast } from "vant";

const router = useRouter()
const route = useRoute()

const userAccount = ref<string>('');
const userPassword = ref<string>('');
const onSubmit = async (values: UserLogin) => {
  const res = await login(values)
  if (res.code === 200) {
    showToast('登录成功')
    window.location.href = '/'
  }
};
</script>

<template>
  <div class="title">请先登录</div>
  <van-form @submit="onSubmit">
    <van-cell-group inset>
      <van-field
          v-model="userAccount"
          name="userAccount"
          label="用户名"
          placeholder="用户名"
          :rules="[{ required: true, message: '请填写用户名' }]"
      />
      <van-field
          v-model="userPassword"
          type="password"
          name="userPassword"
          label="密码"
          placeholder="密码"
          :rules="[{ required: true, message: '请填写密码' }]"
      />
    </van-cell-group>
    <div style="margin: 16px;">
      <van-button round block type="primary" native-type="submit">
        提交
      </van-button>
    </div>
  </van-form>

</template>

<style scoped>
.title {
  color: cornflowerblue;
  font-size: 20px;
  text-align: center;
  margin: 20px 0;
}
</style>