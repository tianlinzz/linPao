import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import Components from 'unplugin-vue-components/vite';
import { VantResolver } from 'unplugin-vue-components/resolvers';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
      vue(),
    //可以直接在模板中使用 Vant 组件了，unplugin-vue-components 会解析模板并自动注册对应的组件。
    Components({
      resolvers: [VantResolver()],
    }),
  ],

})
