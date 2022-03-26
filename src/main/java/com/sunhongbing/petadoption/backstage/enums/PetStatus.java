package com.sunhongbing.petadoption.backstage.enums;

/**
 * @className: PetStatus
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-03-25 14:57
 */
public enum PetStatus {
    /**
     * -1:暂停领养
     * 0:待领养
     * 1:已领养
     * -99:特殊标识，用于查询所有状态
     * 2:认领中
     */

    ALL(-99, "所有状态"),
    PAUSE(-1, "暂停领养"),
    WAIT(0, "待领养"),
    RECEIVE(1, "已领养"),
    CLAIMING(2, "认领中");



    private int code;
    private String desc;

    PetStatus(int code, String desc) {
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

