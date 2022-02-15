package com.sunhongbing.petadoption.backstage.entity;

import lombok.Data;

import java.util.List;

/**
 * @className: SysMenu
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-14 14:29
 */
@Data
public class SysMenu {
    private Integer id;
    private Integer pid;
    private String name;
    private String type;
    private String url;
    private String icon;

    /**
     * 子菜单Menu列表
     */
    private List<SysMenu> childMenu;
}
