import {defineStore} from 'pinia';
import type { StoreDefinition } from 'pinia';
import {UserInfo} from "@/types";
import {updateUserInfo, getCurrentUser} from "@/services/user";

interface State {
    userInfo: UserInfo
}

export const useUserStore: StoreDefinition = defineStore('user', {
    state: (): State => ({
        userInfo: {} as UserInfo
    }),
    actions: {
        async getUserInfo(): Promise<void> {
            if (this.userInfo.id) {
                return // 如果已经有用户信息了，就不再请求
            }
            const res = await getCurrentUser()
            res.data.tags = JSON.parse(res.data.tags) // 序列化标签
            this.userInfo = res.data
        },
        async updateUserInfo(type: string, value: string | number | string[]): Promise<boolean> {
            // @ts-ignore
            this.userInfo[type] = value
            if (type === 'tags') { // 表单提交后，tags是字符串，需要转化成数组
                // 先转化成数组
                // @ts-ignore
                const tagsList = this.userInfo.tags.split(',')
                this.userInfo.tags = JSON.stringify(tagsList)
            }else {
                this.userInfo.tags = JSON.stringify(this.userInfo.tags)
            }
            const res = await updateUserInfo(this.userInfo)
            return res.data
        }
    },
    persist: {
        // If you want to persist only a subset of the store, you can specify it here:
        paths: ['userInfo'],
        // Or if you want to define a custom storage:
        storage: sessionStorage,
        key: 'userInfo'
        // Or if you want to define custom methods to handle the storage:
    }
})