<script setup lang="ts">
import {ref, onMounted} from 'vue'
import {useRouter, useRoute} from "vue-router";
import {showToast} from "vant";
import {CreateTeam} from "@/types";
import dayjs from "dayjs";
import {createTeam, getTeamInfo, updateTeamInfo} from "@/services/team";

const router = useRouter();
const route = useRoute();
const id = route.query.id

onMounted(async () => {
  if (id) {
   const {data, code} = await getTeamInfo(Number(id))
    if (code === 200) {
      name.value = data?.name as string
      maxNum.value = data?.maxNum as number
      description.value = data?.description as string
      status.value = String(data?.status)
      expireTime.value = dayjs(data?.expireTime).format('YYYY-MM-DD HH:mm:ss')
      if (Number(data?.status) === 2) {
        password.value = data?.password as string
      }
    }
  }
})

const name = ref<string>('')
const maxNum = ref<number>(3)
const description = ref<string>('')
const status = ref<string>('0')
const expireTime = ref<string>('')
const minDate = new Date();
const showPicker = ref(false);
const password = ref<string>('')
const onConfirm = ({selectedValues}: { selectedValues: string[] }) => {
  const dateStr: string = selectedValues.join('/');
  const date = new Date(dateStr);
  // 默认设置零点零分零秒
  expireTime.value = dayjs(date).format('YYYY-MM-DD HH:mm:ss');
  showPicker.value = false;
};

const onSubmit = async (values: CreateTeam) => {
  // 转化为后端需要的格式
  const params = {
    ...values,
    expireTime: new Date(values.expireTime),
    status: Number(values.status),
    maxNum: Number(values.maxNum),
  };
  if (id) {
    params.id = Number(id)
  }
  const res = await (id ? updateTeamInfo(params) : createTeam(params));
  if (res.code === 200) {
    showToast({
      message: `${id ? '更新' : '创建'}队伍成功`,
      type: 'success',
    });
    await router.replace('/center/team');
  }
};

</script>

<template>
  <van-form @submit="onSubmit">
    <van-cell-group inset>
      <van-field
          v-model="name"
          name="name"
          label="队伍名称"
          placeholder="请输入队伍名称"
          :rules="[{ required: true, message: '请输入队伍名称' }]"
      />
      <van-field name="maxNum" label="队伍最大人数">
        <template #input>
          <van-stepper
              v-model="maxNum"
              min="3"
              max="10"
          />
        </template>
      </van-field>
      <van-field
          v-model="description"
          rows="4"
          name="description"
          autosize
          label="队伍描述"
          type="textarea"
          maxlength="200"
          placeholder="请输入队伍描述"
          show-word-limit
      />
      <van-field
          v-model="expireTime"
          is-link
          readonly
          name="expireTime"
          label="队伍过期时间"
          placeholder="点击选择队伍过期时间"
          @click="showPicker = true"
      />
      <van-popup v-model:show="showPicker" position="bottom">
        <van-date-picker
            :min-date="minDate"
            @confirm="onConfirm"
            @cancel="showPicker = false"/>
      </van-popup>
      <van-field name="status" label="队伍状态">
        <template #input>
          <van-radio-group v-model="status" direction="horizontal">
            <van-radio name="0">公开</van-radio>
            <van-radio style="margin-right: 4px" name="1">私人</van-radio>
            <van-radio style="margin-left: 6px" name="2">加密</van-radio>
          </van-radio-group>
        </template>
      </van-field>
      <van-field
          v-if="status === '2'"
          v-model="password"
          type="password"
          name="password"
          label="队伍密码"
          placeholder="请填写队伍密码"
          :rules="[{ required: true, message: '请填写队伍密码' }]"
      />
    </van-cell-group>
    <div style="margin: 16px;">
      <van-button round block type="primary" native-type="submit">
        创建
      </van-button>
    </div>
  </van-form>
</template>

<style scoped>

</style>