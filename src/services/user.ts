import request from '@/utils/request';

// 修改个人信息
export const updateUserInfo = (data: any) => {
    return request({
        url: '/user/updateSelf',
        method: 'post',
        data,
    });
}