package com.sunhongbing.petadoption.backstage.service.impl;

import com.sunhongbing.petadoption.backstage.dao.AdminMapper;
import com.sunhongbing.petadoption.backstage.entity.Admin;
import com.sunhongbing.petadoption.backstage.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @className: AdminInfoServiceImpl
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-13 17:34
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminInfoDao;

    @Override
    public Admin findAdminByUsername(String username) {
        return adminInfoDao.findAdminByUsername(username);
    }
}
