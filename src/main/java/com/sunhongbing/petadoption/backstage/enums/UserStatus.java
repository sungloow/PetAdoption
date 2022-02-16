package com.sunhongbing.petadoption.backstage.enums;

public enum UserStatus {
    NORMAL(1, "正常"),
    DISABLE(0, "禁用"),
    DELETE(-1, "删除");

    private final int code;
    private final String desc;

    UserStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
