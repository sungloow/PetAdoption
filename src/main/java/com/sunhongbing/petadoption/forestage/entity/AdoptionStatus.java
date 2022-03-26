package com.sunhongbing.petadoption.forestage.entity;

public enum AdoptionStatus {
    //1:reject, 0:apply, 1:accept
    REJECT(-1, "拒绝"),
    APPLY(0, "申请中"),
    ACCEPT(1, "已通过");

    private int code;
    private String desc;

    AdoptionStatus(int code, String desc) {
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
