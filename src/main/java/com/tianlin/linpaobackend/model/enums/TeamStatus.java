package com.tianlin.linpaobackend.model.enums;

public enum TeamStatus {

    PUBLIC(0, "公开"),
    PRIVATE(1, "私有"),
    SELECT(2, "加密");

    private int code;
    private String desc;

    TeamStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TeamStatus getTeamStatus(int code) {
        for (TeamStatus teamStatus : TeamStatus.values()) {
            if (teamStatus.code == code) {
                return teamStatus;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
