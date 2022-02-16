package com.sunhongbing.petadoption.backstage.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @className: AdminInfo
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-13 17:25
 */
@Data
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private String username;

    private String password;

    private String name;

    private String sex;

    private String tel;

    private String email;

    private String address;

    private int status;
}
