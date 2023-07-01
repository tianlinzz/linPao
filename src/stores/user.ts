import {defineStore} from 'pinia';
import type { StoreDefinition } from 'pinia';
import {UserInfo} from "@/types";
import {updateUserInfo, getCurrentUser} from "@/services/user";
import { showToast } from 'vant';

interface State {
    userInfo: UserInfo
}

export const useUserStore: StoreDefinition = defineStore('user', {
    state: (): State => ({
        userInfo: {} as UserInfo
    }),
    actions: {
        async getUserInfo(): Promise<void> {
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
    }
})