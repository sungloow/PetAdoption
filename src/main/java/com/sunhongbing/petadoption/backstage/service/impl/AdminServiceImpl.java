package com.sunhongbing.petadoption.backstage.service.impl;

import com.sunhongbing.petadoption.backstage.dao.AdminMapper;
import com.sunhongbing.petadoption.backstage.entity.Admin;
import com.sunhongbing.petadoption.backstage.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * @className: AdminInfoServiceImpl
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-13 17:34
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin findAdminByUsername(String username) {
        return adminMapper.findAdminByUsername(username);
    }

    @Override
    public int updatePassword(int id, String password) {
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        return adminMapper.modifyPassword(id, md5Password);
    }
}
