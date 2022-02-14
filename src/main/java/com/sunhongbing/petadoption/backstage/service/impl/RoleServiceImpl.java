package com.sunhongbing.petadoption.backstage.service.impl;

import com.sunhongbing.petadoption.backstage.dao.RoleMapper;
import com.sunhongbing.petadoption.backstage.entity.SysPermission;
import com.sunhongbing.petadoption.backstage.entity.SysRole;
import com.sunhongbing.petadoption.backstage.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @className: RoleServiceImpl
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-13 17:53
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<SysRole> getRoleListByAdminId(String id) {
        return roleMapper.getRoleListByAdminId(id);
    }

    @Override
    public List<SysPermission> getPermissionListByRoleId(int id) {
        return roleMapper.getPermissionListByRoleId(id);
    }
}
