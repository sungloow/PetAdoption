package com.sunhongbing.petadoption.backstage.service.impl;

import com.sunhongbing.petadoption.backstage.dao.PermissionMapper;
import com.sunhongbing.petadoption.backstage.entity.SysPermission;
import com.sunhongbing.petadoption.backstage.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @className: PermissionServiceImpl
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-15 10:57
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    @Override
    public List<SysPermission> getPermissionListByRoleId(int roleId) {
        return permissionMapper.getPermissionListByRoleId(roleId);
    }
}
