import {createApp, App} from 'vue'
import './style.css'
import 'vant/es/toast/style';
import 'vant/es/dialog/style';
import 'vant/es/notify/style';
import 'vant/es/image-preview/style';
import Root from './App.vue'
import router from './routes/index'
import {createPinia, Pinia} from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate' // 引入pinia的持久化插件

const pinia: Pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

const app: App<Element> = createApp(Root)
app.use(router)
app.use(pinia)
app.mount('#app')
