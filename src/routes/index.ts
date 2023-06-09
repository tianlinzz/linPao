import {createRouter, createWebHistory} from 'vue-router'

const index = [
    {
        path: '/',
        redirect: '/home',
        meta: { title: '' }
    },
    {
        path: '/home',
        component: () => import('../pages/Home/index.vue'),
        meta: { title: '主页' }
    },
    {
        path: '/my',
        component: () => import('../pages/My/index.vue'),
        meta: { title: '我的' }
    },
    {
        path: '/team', component: () => import('../pages/Team/index.vue'),
        meta: { title: '队伍' }
    },
]

const router = createRouter({
    history: createWebHistory(),
    routes: [
        ...index
    ]
})

export default router;