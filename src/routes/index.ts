import {createRouter, createWebHistory} from 'vue-router'
import type { Router, RouteRecordRaw } from 'vue-router'

const index: RouteRecordRaw[] = [
    {
        path: '/',
        name: 'index',
        redirect: '/center',
        meta: {title: ''},
    },
    {
        path: '/center',
        redirect: '/center/home',
        name: 'center',
        component: () => import('../components/Layout/index.vue'),
        meta: {title: '交友中心'},
        children: [
            {
                path: 'home',
                name: 'home',
                component: () => import('../pages/Home/index.vue'),
                meta: {title: '主页'}
            },
            {
                path: 'my',
                name: 'my',
                component: () => import('../pages/My/index.vue'),
                meta: {title: '我的'}
            },
            {
                path: 'team',
                name: 'team',
                component: () => import('../pages/Team/index.vue'),
                meta: {title: '队伍'}
            },
            {
                path: 'search',
                name: 'search',
                component: () => import('../pages/Search/index.vue'),
                meta: {title: '搜索'}
            },
            {
                path: 'edit',
                name: 'edit',
                component: () => import('../pages/My/Edit.vue'),
                meta: {title: '编辑'}
            },
            {
                path: 'result',
                name: 'result',
                component: () => import('../pages/Search/Result.vue'),
                meta: {title: '搜索结果'}
            }
        ]
    },
    {
        path: '/login',
        name: 'login',
        component: () => import('../pages/Login/index.vue'),
        meta: {title: '登录'}
    },
]

const router: Router = createRouter({
    history: createWebHistory(),
    routes: [
        ...index
    ]
})

export default router;