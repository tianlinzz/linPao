package com.tianlin.linpaobackend.model.enums;

import java.util.Objects;

public enum TeamStatus {

    PUBLIC(0, "公开"),
    PRIVATE(1, "私有"),
    SELECT(2, "加密");

    private Integer code;
    private String desc;

    TeamStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TeamStatus getTeamStatus(Integer code) {
        for (TeamStatus teamStatus : TeamStatus.values()) {
            if (Objects.equals(teamStatus.code, code)) {
                return teamStatus;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
