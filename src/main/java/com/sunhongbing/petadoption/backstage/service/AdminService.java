package com.sunhongbing.petadoption.backstage.service;

import com.sunhongbing.petadoption.backstage.entity.Admin;

public interface AdminService {
    Admin findAdminByUsername(String username);

    //修改密码
    int updatePassword(int id, String password);
}
