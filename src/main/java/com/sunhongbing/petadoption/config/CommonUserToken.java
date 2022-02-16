package com.sunhongbing.petadoption.config;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @className: CommonUserToken
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-15 17:22
 */
public class CommonUserToken extends UsernamePasswordToken {
    public CommonUserToken(String username, String password) {
        super(username, password);
    }
}
