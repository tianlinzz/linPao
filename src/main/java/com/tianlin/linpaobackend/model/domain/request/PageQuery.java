package com.tianlin.linpaobackend.model.domain.request;

import lombok.Data;

import java.util.List;


@Data
public class PageQuery<T> {

    private final long total;
    private final List<T> items;

    public PageQuery(long total, List<T> items) {
        this.total = total;
        this.items = items;
    }
}
