package com.tianlin.linpaobackend.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 队伍请求参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TeamQuery extends PageQuery {

    private static final long serialVersionUID = 2093993638587721622L;
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
     * 搜索关键词
     */
    private String keyword;

    /**
     * (队伍为加密状态)队伍密码
     */
    private String password;

    /**
     * 是否只查询加入的队伍
     */
    private Boolean isOnlyJoin;
}
