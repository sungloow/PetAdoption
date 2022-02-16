package com.sunhongbing.petadoption.backstage.service.impl;

import com.sunhongbing.petadoption.backstage.dao.UserMapper;
import com.sunhongbing.petadoption.backstage.entity.User;
import com.sunhongbing.petadoption.backstage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @className: UserServiceImpl
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-16 15:46
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User findUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }
}