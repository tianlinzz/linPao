package com.tianlin.linpaobackend.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 退出队伍请求类
 * @author 天琳
 * */
@Data
public class TeamQuitRequest implements Serializable {

    private static final long serialVersionUID = 3247049506273129151L;
    /**
     * 队伍id
     * */
    private long teamId;
}
