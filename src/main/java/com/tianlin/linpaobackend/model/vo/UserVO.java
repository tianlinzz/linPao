package com.tianlin.linpaobackend.model.vo;

import lombok.Data;

@Data
public class UserVO {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户编码
     */
    private Long userCode;

    /**
     * 用户标签列表
     */
    private String tags;
}
