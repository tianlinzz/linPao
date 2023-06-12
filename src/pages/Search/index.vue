<script setup lang="ts">
import {ref, onMounted} from 'vue';
import {showToast} from 'vant';
import type {Item} from '../../types'


onMounted(() => {
    searchInput.value?.focus(); // 自动聚焦
});

const searchValue = ref<String>();
const onSearch = (val: any) => {
  const allTags = items.flatMap((item) => item.children); // 扁平化数组
  const result: Item[] = allTags.filter((item) => item.text.includes(val));
  // 搜索后自动聚焦
  searchInput.value?.focus();
  if (result.length === 0) {
    searchInput.value?.focus();
    showToast('未搜索到相关标签');
    return;
  }
  // activeIdList没有的标签才添加
  result.forEach((item: Item) => {
    if (!activeIdList.value.includes(item.id)) {
      activeIdList.value.push(item.id);
      return;
    }
    showToast('已添加该标签')
  });
};
const onCancel = () => {
  searchValue.value = '';
  searchInput.value?.focus();
};

const searchInput = ref<HTMLHtmlElement | null>();

const activeIdList = ref<string []>([]); // 选中的标签id列表
const activeIndex = ref<number>(0);// 默认展开的标签索引
const items = [
  {
    text: '浙江',
    children: [
      { text: '杭州', id: '杭州' },
      { text: '温州', id: '温州' },
      { text: '宁波', id: '宁波' },
      { text: '义乌', id: '义乌' },
    ],
  },
  {
    text: '江苏',
    children: [
      { text: '南京', id: '南京' },
      { text: '苏州', id: '苏州' },
      { text: '常州', id: '常州' },
      { text: '淮安', id: '淮安' },
      { text: '扬州', id: '扬州' },
      { text: '南通', id: '南通' },
      { text: '镇江', id: '镇江' },
      { text: '泰州', id: '泰州' },
      { text: '无锡', id: '无锡' },
      { text: '徐州', id: '徐州' },
    ],
  },
  {
    text: '福建',
    children: [
      {text: '福州', id: '福州'},
      {text: '厦门', id: '厦门'},
      {text: '莆田', id: '莆田'},
      {text: '三明', id: '三明'},
      {text: '泉州', id: '泉州'},
      {text: '漳州', id: '漳州'},
    ],
  }
];

const removeItem = (id: string) => {
  const index = activeIdList.value.indexOf(id);
  if (index > -1) {
    activeIdList.value.splice(index, 1);
  }
}


</script>

<template>
  <van-search
      :ref="(el) => (searchInput = el)"
      v-model="searchValue"
      show-action
      placeholder="请输入搜索的标签"
      @search="onSearch"
      @cancel="onCancel"
  />
  <div class="content">
    <van-divider
        :style="{ color: '#1989fa', borderColor: '#1989fa' }"
    >已选标签</van-divider>

      <span v-if="activeIdList.length === 0" :style="{ color: 'gray' }">请选择至少一个标签</span>
      <van-tag
          :style="{ margin: '4px' }"
          v-for="item in activeIdList"
          :key="item"
          type="primary"
          :closeable="true"
          @close="removeItem(item as string)"
      >{{ item }}</van-tag>

    <van-divider
        :style="{ color: '#1989fa', borderColor: '#1989fa' }"
    >全部标签</van-divider>

    <van-tree-select
        v-model:active-id="activeIdList"
        v-model:main-active-index="activeIndex"
        :items="items"
    />
  </div>

</template>

<style scoped>
.content {
  padding: 0 16px;
}
</style>