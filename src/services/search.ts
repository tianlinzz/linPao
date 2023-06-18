import request from '@/utils/request';

export const getFriendList = (tagNameList: String[]) => {
    return request({
        url: '/user/friendList?tagNameList=' + tagNameList,
        method: 'get',
    });
}