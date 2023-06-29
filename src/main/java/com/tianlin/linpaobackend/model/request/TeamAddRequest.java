package com.tianlin.linpaobackend.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class TeamAddRequest {
    /**
     * 队伍名称
     */
    private String name;

    /**
     * 队伍描述
     */
    private String description;

    /**
     * 最大人数，默认为5
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 队伍状态 0公开 1私密但是无密码 2加密
     */
    private Integer status;

    /**
     * 队伍密码
     */
    private String password;
}
