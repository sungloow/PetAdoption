package com.sunhongbing.petadoption.backstage.service;

import com.sunhongbing.petadoption.backstage.entity.AdminInfo;

public interface AdminInfoService {
    public AdminInfo findAdminByUsername(String username);
}
