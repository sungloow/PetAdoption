package com.sunhongbing.petadoption.forestage.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @className: User
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-16 15:41
 */
@Data
public class EditParam implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    //性别
    private String sex;
    //年龄
    private int age;
    //电话
    private String tel;
    //email
    private String email;
    //地址
    private String address;
    //养宠经验
    private int experience;
}
