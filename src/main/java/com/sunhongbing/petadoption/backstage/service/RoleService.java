package com.sunhongbing.petadoption.backstage.service;

import com.sunhongbing.petadoption.backstage.entity.SysRole;

import java.util.List;

public interface RoleService {
    List<SysRole> getRoleListById(int id);
}
