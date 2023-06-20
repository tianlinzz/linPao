<script setup lang="ts">
import {onMounted, ref} from "vue";
import {useRoute, useRouter} from 'vue-router'
import { updateUserInfo } from "@/services/user";
import { showToast } from 'vant'
import {ResponseData} from "@/types";

const router = useRouter()
const route = useRoute()
const {type, currentValue} = route.query


onMounted(() => {
  if (type === 'tags') {
    value.value = JSON.stringify(currentValue)
  }
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
const onSubmit = async () => {
  if (type === 'tags') {
    // 把字符串转换成数组
    const tagStr: string = value.value as string
    const tagArr: string[] = tagStr.split(',')
    value.value = JSON.stringify(tagArr)
  }
  const payload = {
    id: route.query.id,
    type,
    value: value.value,
  }
  const res: ResponseData =  await updateUserInfo(payload)
  if (res.data) {
    showToast({
      message: '修改成功',
      type: 'success'
    })
    router.back()
    return;
  }
  showToast({
    message: res?.description || res?.msg || '修改失败',
    type: 'fail'
  })
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

<style scoped lang="scss">
</style>