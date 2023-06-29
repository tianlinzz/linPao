package com.tianlin.linpaobackend.model.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TeamUserVO {
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
     * 过期时间
     */
    private Date expireTime;

    /**
     * 队伍状态 0公开 1私密但是无密码 2加密
     */
    private Integer status;

    /**
     * 加入的用户列表
     */
    private List<UserVO> userList;
}
