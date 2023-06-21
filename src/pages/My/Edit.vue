<script setup lang="ts">
import {onMounted, ref} from "vue";
import {useRoute, useRouter} from 'vue-router'
import { showToast } from 'vant'
import {useUserStore} from '@/stores/user'

const userStore = useUserStore()
const { updateUserInfo } = userStore

const router = useRouter()
const route = useRoute()
const {type, currentValue} = route.query


onMounted(() => {
  value.value = currentValue as (string | number | string[])
})

const typeMap = {
  'username': '昵称',
  'avatarUrl': '头像',
  'gender': '性别',
  'phone': '手机号',
  'email': '邮箱',
  'tags': '标签'
}

const value = ref<string | number | string[]>();
const onSubmit = async () => {
  const result = updateUserInfo(type, value.value);
  if (result) {
    showToast({
      message: '修改成功',
      type: 'success'
    });
    router.back();
    return;
  }
  showToast({
    message: '修改失败',
    type: 'fail'
  });
  return;
};
</script>

<template>
  <van-form @submit="onSubmit">
    <van-cell-group inset>
      <van-field
          v-model="value"
          :label="typeMap[type]"
          placeholder="请输入修改的内容"
          :rules="[{ required: true, message: '请输入修改的内容' }]"
      />
    </van-cell-group>
    <div style="margin: 16px;">
      <van-button round block type="primary" native-type="submit">
        提交
      </van-button>
    </div>
  </van-form>
</template>

<style scoped lang="scss">
</style>