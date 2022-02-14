package com.sunhongbing.petadoption.backstage.service;

import com.sunhongbing.petadoption.backstage.entity.SysPermission;
import com.sunhongbing.petadoption.backstage.entity.SysRole;

import java.util.List;

public interface RoleService {
    public List<SysRole> getRoleListByAdminId(String id);
    List<SysPermission> getPermissionListByRoleId(int id);
}
