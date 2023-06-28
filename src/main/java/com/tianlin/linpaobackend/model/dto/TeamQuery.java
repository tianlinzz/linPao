package com.tianlin.linpaobackend.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;


/**
 * 队伍请求参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TeamQuery extends PageQuery {
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
}
