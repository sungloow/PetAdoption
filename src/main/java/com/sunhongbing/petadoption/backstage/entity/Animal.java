package com.sunhongbing.petadoption.backstage.entity;

import lombok.Data;


/**
 * @className: Animal
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-03-02 16:01
 */
@Data
public class Animal {
    private int id;
    private String name;
    private String sex;
    private String type;
    private String birth;
    private int age;
    private String detail;
    private String pics;
    private int status;

}
