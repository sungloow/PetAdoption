package com.sunhongbing.petadoption.backstage.service;

import com.sunhongbing.petadoption.backstage.entity.SysPermission;

import java.util.List;

public interface PermissionService {
    List<SysPermission> getPermissionListByRoleId(int roleId);
}
