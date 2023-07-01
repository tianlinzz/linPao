package com.tianlin.linpaobackend.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 队伍加入请求类
 * @author 天琳
 * */
@Data
public class TeamJoinRequest implements Serializable {

    private static final long serialVersionUID = 1350959423901784838L;

    /**
     * 队伍id
     * */
    private long teamId;

    /**
     * 如果是加密队伍，队伍密码
     * */
    private String password;
}
