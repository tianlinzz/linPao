import {defineStore} from 'pinia'
import type { StoreDefinition } from 'pinia'

interface State {
    title: string
    activeTabbar: string,
    showTabbar: boolean
}

export const useTitleStore: StoreDefinition = defineStore('title', {
    state: (): State => ({
        title: '',
        activeTabbar: 'home',
        showTabbar: true
    }),
    actions: {
        setTitle(title: string) {
            this.title = title
        },
        setActive(name: string) {
            this.activeTabbar = name
        },
        setShowTabbar(show: boolean) {
            this.showTabbar = show
        }
    },
})