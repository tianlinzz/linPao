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
// 查询团队信息
export function getTeamInfo(id: number): Promise<ResponseData<TeamInfo>> {
    return request({
        url: "/team/get",
        method: "get",
        params: {id}
    });
}
// 更新团队信息
export function updateTeamInfo(data: CreateTeam): Promise<ResponseData<boolean>> {
    return request({
        url: "/team/update",
        method: "post",
        data
    });
}

// 删除团队
export function dissolveTeam(teamId: number): Promise<ResponseData<boolean>> {
    return request({
        url: "/team/dissolve",
        method: "post",
        data: {teamId}
    });
}

// 加入队伍
export function joinTeamAPI(data: { teamId: number, password?: string }): Promise<ResponseData<boolean>> {
    return request({
        url: "/team/join",
        method: "post",
        data,
    });
}

// 退出队伍
export function quitTeamAPI(teamId: number): Promise<ResponseData<boolean>> {
    return request({
        url: "/team/quit",
        method: "post",
        data: {teamId}
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