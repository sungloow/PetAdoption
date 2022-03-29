package com.sunhongbing.petadoption.backstage.service;

import com.sunhongbing.petadoption.backstage.entity.SysPermission;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PermissionService {
    List<SysPermission> getPermissionListByRoleId(int roleId);


    //获取所有权限
    List<SysPermission> getAllPermission();
}
