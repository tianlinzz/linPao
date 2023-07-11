package com.tianlin.linpaobackend.constant;

/**
 * 用户常量
 */
public interface UserConstant {

    /**
     * 用户登录状态
     */
    String USER_LOGIN_PREFIX = "linpao:user:login:status";

    /**
     * 用户角色状态
     */
    int DEFAULT_ROLE = 0; // 默认角色
    int ADMIN_ROLE = 1; // 管理员角色

    /**
     * 超时时间
     */
    int TIME_OUT = 60 * 60 * 24 * 7; // 7天

    String REDIS_USER_PREFIX = "linpao:user";

    String REDIS_TEAM_PREFIX = "linpao:team";
}
