package com.tianlin.linpaobackend.model.dto;

import lombok.Data;

import java.io.Serializable;


/**
 * 分页查询参数
 */
@Data
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 8727585828695381329L;

    /**
     * 每页数量
     */
    protected int pageSize = 10;

    /**
     * 当前页面
     */
    protected int pageNum = 1;
}
