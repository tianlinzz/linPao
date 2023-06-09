import { defineStore } from 'pinia'

export const useTitleStore = defineStore('title', {
    state: () => ({
        title: '主页'
    }),
    actions: {
        setTitle(title: string) {
            this.title = title
        }
    },
})