package com.tianlin.linpaobackend.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class TeamInfoResponse {
    /**
     * 队伍id
     */
    private Long id;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 队伍描述
     */
    private String description;

    /**
     * 队长id
     */
    private Long userId;

    /**
     * 最大人数，默认为5
     */
    private Integer maxNum;

    /**
     * 队伍状态 0公开 1私密但是无密码 2加密
     */
    private Integer status;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * (队伍为加密状态)队伍密码
     */
    private String password;
}
