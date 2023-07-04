import request from "@/utils/request";
import {CreateTeam, GetTeamList, ResponseData, TeamInfo} from "@/types";

// 创建团队
export function createTeam(data: CreateTeam): Promise<ResponseData<boolean>> {
    return request({
        url: "/team/create",
        method: "post",
        data
    });
}
// 查询团队列表
export function getTeamList(params?: GetTeamList): Promise<ResponseData<TeamInfo>> {
    return request({
        url: "/team/list",
        method: "get",
        params
    });
}