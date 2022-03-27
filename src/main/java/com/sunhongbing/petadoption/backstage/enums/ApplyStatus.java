package com.sunhongbing.petadoption.backstage.enums;

/**
 * @className: PetStatus
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-03-25 14:57
 */
public enum ApplyStatus {


    ALL(-99, "所有状态"),
    REJECT(-1, "拒绝"),
    APPLY(0, "申请中"),
    PASS(1, "通过");


    private int code;
    private String desc;

    ApplyStatus(int code, String desc) {
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

