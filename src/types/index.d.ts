export type UserInfo = {
    id?: number;
    username?: string;
    avatarUrl?: string;
    userAccount?: string;
    email?: string;
    gender?: number;
    phone?: string;
    userStatus?: number;
    createTime?: Date;
    updateTime?: Date;
    userRole?: number;
    userCode?: number;
    tag?: string [];
};

export type Item = {
    text: string;
    id: string;
}