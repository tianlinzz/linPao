import {createRouter, createWebHistory} from 'vue-router'

const index = [
    {
        path: '/',
        redirect: '/center',
        meta: {title: ''},
    },
    {
        path: '/center',
        redirect: '/center/home',
        component: () => import('../components/Layout/index.vue'),
        meta: {title: '交友中心'},
        children: [
            {
                path: 'home',
                component: () => import('../pages/Home/index.vue'),
                meta: {title: '主页'}
            },
            {
                path: 'my',
                component: () => import('../pages/My/index.vue'),
                meta: {title: '我的'}
            },
            {
                path: 'team', component: () => import('../pages/Team/index.vue'),
                meta: {title: '队伍'}
            },
        ]
    },
    {
        path: '/login',
        component: () => import('../pages/Login/index.vue'),
        meta: {title: '登录'}
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes: [
        ...index
    ]
})

export default router;