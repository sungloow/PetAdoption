package com.sunhongbing.petadoption.forestage.entity;

import lombok.Data;

/**
 * @className: ApplyRecord
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-03-26 20:33
 */
@Data
public class ApplyRecord {
    private Integer petId;
    private Integer userId;
    private Integer status;
    private String time;
}
