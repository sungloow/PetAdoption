package com.sunhongbing.petadoption.backstage.enums;

/**
 * @className: PetStatus
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-03-25 14:57
 */
public enum ArticleStatus {


    ALL(-99, "所有状态"),
    REJECT(-1, "不通过"),
    APPLY(0, "审核中"),
    PASS(1, "已发布");


    private int code;
    private String desc;

    ArticleStatus(int code, String desc) {
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

