package com.sunhongbing.petadoption.backstage.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @className: Article
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-03-27 17:25
 */
@Data
public class Banner implements Serializable {

    private int id; //文章的唯一ID
    private String author; //作者名
    private String title; //标题
    //创建时间
    private String createTime;
    private String content; //文章的内容
    private String cover; //封面图片
}
