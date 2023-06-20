import request from '@/utils/request';


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