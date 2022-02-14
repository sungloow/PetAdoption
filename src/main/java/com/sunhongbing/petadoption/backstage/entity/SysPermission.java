package com.sunhongbing.petadoption.backstage.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @className: SysPermission
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-14 14:04
 */
@Data
public class SysPermission implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private String permission;
}
