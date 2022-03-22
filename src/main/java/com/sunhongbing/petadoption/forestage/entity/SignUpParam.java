package com.sunhongbing.petadoption.forestage.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @className: SignUpParam
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-18 10:24
 */
@Data
public class SignUpParam implements Serializable {
    private static final long serialVersionUID = -8141829994735551355L;

    private String email;
    private String password;
    private String confirmPassword;
    private String tel;
    private String name;
    private int age;
    private String sex;
    private String address;
    private int experience;
}
