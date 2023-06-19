import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import Components from 'unplugin-vue-components/vite';
import {VantResolver} from 'unplugin-vue-components/resolvers';
import {resolve} from 'path'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    //可以直接在模板中使用 Vant 组件了，unplugin-vue-components 会解析模板并自动注册对应的组件。
    Components({
      resolvers: [VantResolver()],
    }),
    import('postcss-px-to-viewport') // vite3+ 不在支持commonJs规范，所以需要使用import()动态导入，使用ES6规范，并且文件后缀是cjs
  ],
  resolve:{
    alias:{
      '@':resolve('src')
    }
  },
  // server: {
  //   proxy: {
  //     '/api': {
  //       target: 'http://127.0.0.1:8080',
  //       changeOrigin: true,
  //       rewrite: (path) => path.replace(/^\/api/, ''),
  //     },
  //   },
  // },
})
