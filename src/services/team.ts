import request from "@/utils/request";
import {CreateTeam, ResponseData} from "@/types";

// 创建团队
export function createTeam(data: CreateTeam): Promise<ResponseData> {
    return request({
        url: "/team/create",
        method: "post",
        data
    });
}