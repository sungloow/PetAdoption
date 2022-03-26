package com.sunhongbing.petadoption.forestage.entity;

import lombok.Data;

import java.util.Date;

/**
 * @className: Animal
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-03-02 16:01
 */
@Data
public class ApplyAnimal {

    private int status;
    private String time;
    private int id;
    private String name;
    private String sex;
    private String type;
    private Date birth;
    private int age;
    private String detail;
    private String pics;


}
