import request from '@/utils/request';
import {ResponseData, UserLogin} from "@/types";


// 登录
export const login : (data: UserLogin) => Promise<ResponseData> = (data) => {
    return request({
        url: '/user/login',
        method: 'post',
        data,
    });
}

// 获取当前用户
export const getCurrentUser = () => {
    return request({
        url: '/user/current',
        method: 'get',
    });
}

// 修改个人信息
export const updateUserInfo = (data: any) => {
    return request({
        url: '/user/update',
        method: 'post',
        data,
    });
}

// 推荐用户
export const getRecommendUser = (size: number = 10, page: number = 1) => {
    return request({
        url: `/user/recommend/${size}/${page}`,
        method: 'get',
    });
}