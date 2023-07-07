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

export type ResponseData<T =any> = {
    code?: number;
    data?: T;
    description?: string;
    msg?: string;
}

export type UserLogin = {
    userAccount: string;
    userPassword: string;
}

export type CreateTeam = {
    id?: number,
    description: string,
    expireTime: Date,
    maxNum: number,
    name: string,
    password?: string,
    status: number
}

export type GetTeamList = {
    id?: number,
    description?: string,
    keyword?: string,
    maxNum?: number,
    name?: string,
    status?: number,
    userId?: number,
    isOnlyCreate?: boolean,
    isOnlyJoin?: boolean,
    isOnlyNotJoin?: boolean,
    statusList?: number[],
}

export type TeamInfo = {
    id?: number,
    name?: string,
    description?: string,
    maxNum?: number,
    expireTime?: Date,
    status?: number,
    userId?: number,
    password?: string,
    createUser?: UserInfo,
    userList?: UserInfo[],
}

export type JoinTeam = {
    teamId: number,
    password?: string
}

