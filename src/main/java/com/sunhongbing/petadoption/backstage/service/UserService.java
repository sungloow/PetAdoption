package com.sunhongbing.petadoption.backstage.service;

import com.sunhongbing.petadoption.backstage.entity.User;

public interface UserService {
    public User findUserByEmail(String username);
}
