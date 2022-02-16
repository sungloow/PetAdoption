package com.sunhongbing.petadoption.backstage.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @className: LoginParam
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-14 11:34
 */
@Data
public class LoginParam implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
}
