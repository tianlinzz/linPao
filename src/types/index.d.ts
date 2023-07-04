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
    tags?: string;
};

export type Item = {
    text: string;
    id: string;
}

export type ResponseData = {
    code?: number;
    data?: any;
    description?: string;
    msg?: string;
}

export type UserLogin = {
    userAccount: string;
    userPassword: string;
}