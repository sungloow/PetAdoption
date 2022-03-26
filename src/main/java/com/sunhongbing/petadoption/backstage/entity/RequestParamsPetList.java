package com.sunhongbing.petadoption.backstage.entity;

import lombok.Data;

/**
 * @className: RequestParams
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-03-23 18:13
 */
@Data
public class RequestParamsPetList {
    private String sort;//排序的字段名 id
    private String order;//排序的方式:desc|asc
    private Integer pageNumber;//偏移量，从第几条数据开始显示:0
    private Integer pageSize;//一页显示的条数:5
    private int search_status;//搜索的关键字
}
