import {defineStore} from 'pinia'
import type { StoreDefinition } from 'pinia'

export const useTitleStore: StoreDefinition = defineStore('title', {
    state: () => ({
        title: '主页'
    }),
    actions: {
        setTitle(title: string) {
            this.title = title
        }
    },
})