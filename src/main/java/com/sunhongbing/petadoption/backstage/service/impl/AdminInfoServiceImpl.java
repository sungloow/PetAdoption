package com.sunhongbing.petadoption.backstage.service.impl;

import com.sunhongbing.petadoption.backstage.dao.AdminInfoMapper;
import com.sunhongbing.petadoption.backstage.entity.AdminInfo;
import com.sunhongbing.petadoption.backstage.service.AdminInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @className: AdminInfoServiceImpl
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-13 17:34
 */
@Service
public class AdminInfoServiceImpl implements AdminInfoService {
    @Autowired
    private AdminInfoMapper adminInfoDao;

    @Override
    public AdminInfo findAdminByUsername(String username) {
        return adminInfoDao.findAdminByUsername(username);
    }
}
