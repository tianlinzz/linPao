package com.tianlin.linpaobackend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class TeamUserVO implements Serializable {

    private static final long serialVersionUID = -7134929730104131983L;
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
     * 创建队伍的用户
     */
    private UserVO createUser;
    /**
     * 队伍成员
     */
    private List<UserVO> userList;
}
