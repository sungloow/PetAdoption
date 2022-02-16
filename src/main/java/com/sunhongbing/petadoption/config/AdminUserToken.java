package com.sunhongbing.petadoption.config;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @className: AdminUserToken
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-15 17:22
 */
public class AdminUserToken extends UsernamePasswordToken {
    public AdminUserToken(String username, String password) {
        super(username, password);
    }
}
