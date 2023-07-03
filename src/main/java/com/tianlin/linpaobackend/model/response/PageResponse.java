package com.tianlin.linpaobackend.model.response;

import lombok.Data;

import java.util.List;


@Data
public class PageResponse<T> {

    private long total;
    private List<T> items;
}
