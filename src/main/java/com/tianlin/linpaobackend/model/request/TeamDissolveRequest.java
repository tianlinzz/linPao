package com.tianlin.linpaobackend.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 退出队伍请求类
 * @author 天琳
 * */
@Data
public class TeamDissolveRequest implements Serializable {

    private static final long serialVersionUID = -8050774838567682600L;
    /**
     * 队伍id
     */
    private long teamId;
}
