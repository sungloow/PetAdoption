package com.sunhongbing.petadoption.backstage.enums;

/**
 * @className: PetStatus
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-03-25 14:57
 */
public enum ArticleType {

    /**
     * 文章类型
     */
    ALL(-1, "全部"),
    NEWS(0, "新闻"),
    //活动
    ACTIVITY(1, "活动"),
    //公告
    NOTICE(2, "公告"),
    //文章
    ARTICLE(3, "文章"),
    //快乐领养
    HAPPY(4, "快乐领养");


    private int code;
    private String desc;

    ArticleType(int code, String desc) {
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

