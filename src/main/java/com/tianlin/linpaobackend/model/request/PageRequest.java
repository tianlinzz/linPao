package com.tianlin.linpaobackend.model.request;

import lombok.Data;

import java.util.List;


@Data
public class PageRequest<T> {

    private final long total;
    private final List<T> items;

    public PageRequest(long total, List<T> items) {
        this.total = total;
        this.items = items;
    }
}
