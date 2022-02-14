package com.sunhongbing.petadoption.backstage.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @className: SysRole
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-13 17:30
 */
@Data
public class SysRole implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String description;
    private String role;
}
