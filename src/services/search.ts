import request from '@/utils/request';

export const getFriendList = (tagNameList: String[]) => {
    return request({
        url: `/user/friendList?tags=${tagNameList}`,
        method: 'get',
    });
}