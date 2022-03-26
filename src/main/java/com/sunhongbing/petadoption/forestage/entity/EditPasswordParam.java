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
public class EditPasswordParam implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String oldPwd;
    private String newPwd;
    private String confirmPwd;

}
