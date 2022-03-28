package com.sunhongbing.petadoption.backstage.service;

import com.sunhongbing.petadoption.backstage.entity.User;

public interface UserService {
    User findUserByEmail(String username);

    User getUserById(int userId);
}
