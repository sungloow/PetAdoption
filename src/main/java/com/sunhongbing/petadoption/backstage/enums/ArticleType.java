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
    HAPPY(4, "快乐领养"),
    // 反馈 - 一般问题
    FEEDBACK_GENERAL(5,"反馈-一般问题"),
    // 反馈 - 领养问题
    FEEDBACK_ADOPTION(6,"反馈-领养问题"),
    // 反馈 - 投诉问题
    FEEDBACK_COMPLAINT(7,"反馈-投诉"),
    // 反馈 - 其他问题
    FEEDBACK_OTHER(8,"反馈-其他问题"),
    // 领养-领养方法
    ADOPTION_METHOD(9,"领养-领养方法"),
    // 关于我们-组织介绍
    ABOUT_ORGANIZATION(10,"关于我们-组织介绍"),
    // 关于我们-时间地点
    ABOUT_TIME_PLACE(11,"关于我们-时间地点"),
    // 关于我们-常见问答
    ABOUT_QUESTION(12,"关于我们-常见问答");




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

