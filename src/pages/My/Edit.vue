<script setup lang="ts">
import {ref, onMounted} from "vue";
import { useRoute } from 'vue-router'
import {UserInfo} from "../../types";

const route = useRoute()
const {type, currentValue} = route.query


onMounted(() => {
  value.value = currentValue as (string | number)
})

const typeMap = {
  'username': '昵称',
  'avatarUrl': '头像',
  'gender': '性别',
  'phone': '手机号',
  'email': '邮箱',
  'tags': '标签'
}

const value = ref<string | number>();
const onSubmit = (values: UserInfo) => {
  console.log('submit', values);
};

</script>

<template>
  <van-form @submit="onSubmit">
    <van-cell-group inset>
      <van-field
          v-model="value"
          name="用户名"
          :label="typeMap[type]"
          placeholder="用户名"
          :rules="[{ required: true, message: '请填写用户名' }]"
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

</style>