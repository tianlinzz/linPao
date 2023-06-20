import request from '@/utils/request';

export const getFriendList = (tagNameList: String[]) => {
    return request({
        url: `/user/friendList?tags=${tagNameList}`,
        method: 'get',
    });
}

// 获取当前用户
export const getCurrentUser = () => {
    return request({
        url: '/user/current',
        method: 'get',
    });
}