<script setup lang="ts">
import {onMounted, ref} from 'vue';
import {useRouter} from 'vue-router'
import {showToast} from 'vant';
import type {Item} from '../../types'

const router = useRouter();

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
    text: '语言',
    children: [
      { text: 'java', id: 'java' },
      { text: 'python', id: 'python' },
      { text: 'javascript', id: 'javascript' },
      { text: 'c++', id: 'c++' },
    ],
  },
  {
    text: '框架',
    children: [
      {
        text: 'spring',
        id: 'spring',
      },
      {
        text: 'spring boot',
        id: 'spring boot',
      },
      {
        text: 'react',
        id: 'react',
      },
      {
        text: 'vue',
        id: 'vue',
      }
    ]
  },
  {
    text: '状态',
    children: [
      {
        text: '开心',
        id: '开心',
      },
      {
        text: 'emo',
        id: 'emo',
      },
      {
        text: '学习中',
        id: '学习中',
      },
      {
        text: '烦躁',
        id: '烦躁',
      },
    ]
  }
];

const removeItem = (id: string) => {
  const index = activeIdList.value.indexOf(id);
  if (index > -1) {
    activeIdList.value.splice(index, 1);
  }
}

const goToResult = () => {
  if (activeIdList.value.length === 0) {
    showToast('请选择至少一个标签');
    return;
  }
  router.push({
    name: 'result',
    query: {
      tags: JSON.stringify(activeIdList.value)
    }
  })
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
    >
      <template #action>
        <van-button @click="goToResult" style="margin-bottom: 5px" type="primary" size="small">匹配伙伴</van-button>
      </template>
    </van-search>
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