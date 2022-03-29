package com.sunhongbing.petadoption.backstage.service.impl;

import com.sunhongbing.petadoption.backstage.dao.AdminMapper;
import com.sunhongbing.petadoption.backstage.dao.RoleMapper;
import com.sunhongbing.petadoption.backstage.entity.SysPermission;
import com.sunhongbing.petadoption.backstage.entity.SysRole;
import com.sunhongbing.petadoption.backstage.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public List<SysRole> getRoleListById(int id) {
        id = id<=100?id:0;
        return roleMapper.getRoleListById(id);
    }

    @Override
    public SysRole getRoleById(int id) {
        return roleMapper.getRoleById(id);
    }

    @Override
    public List<SysRole> getAllRole(String order, String sort) {
        return roleMapper.getAllRole(order,sort);
    }

    @Override
    public int addRole(SysRole role) {
        return roleMapper.addRole(role);
    }

    @Transactional(isolation = Isolation.DEFAULT)
    @Override
    public int modifyRole(int id, List<Integer> newMenuIds, List<Integer> newPermissions) {
        //查询角色关联的菜单
        List<Integer> oldMenuIds = roleMapper.getMenuIdByRoleId(id);
        //查询角色关联的权限
        List<Integer> oldPermissionIds = roleMapper.getPermissionIdByRoleId(id);

        List<Integer> ids = new ArrayList<>();
        ids.add(id);

        //发生异常则事务回滚
        try {
            //如果菜单有变化，则删除原有菜单，重新绑定新菜单
            if(!oldMenuIds.equals(newMenuIds)){
                //如果新菜单为空，则删除原有菜单，不绑定新菜单
                if(newMenuIds.size()==0 && oldMenuIds.size()!=0){
                    roleMapper.deleteRoleMenus(ids);
                }
                //如果旧菜单为空，则不删除原有菜单，绑定新菜单
                else if(oldMenuIds.size()==0 && newMenuIds.size()!=0){
                    roleMapper.bindMenus(id,newMenuIds);
                }
                //如果新旧菜单都不为空，则删除原有菜单，绑定新菜单
                else if(oldMenuIds.size() != 0){
                    roleMapper.deleteRoleMenus(ids);
                    roleMapper.bindMenus(id,newMenuIds);
                }
            }
            //权限部分
            if(!oldPermissionIds.equals(newPermissions)){
                //如果新权限为空,旧权限不为空，则删除原有权限，不绑定新权限
                if(newPermissions.size()==0 && oldPermissionIds.size()!=0){
                    roleMapper.deleteRolePermissions(ids);
                }
                //如果旧权限为空,新权限不为空，则不删除原有权限，绑定新权限
                else if(oldPermissionIds.size()==0 && newPermissions.size()!=0){
                    roleMapper.bindPermissions(id,newPermissions);
                }
                //如果新旧权限都不为空，则删除原有权限，绑定新权限
                else if(oldPermissionIds.size() != 0){
                    roleMapper.deleteRolePermissions(ids);
                    roleMapper.bindPermissions(id,newPermissions);
                }
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deleteRole(int id) {
        List<Integer> ids = new ArrayList<>();
        ids.add(id);
        try {
            //删除角色
            roleMapper.deleteRoles(ids);
            //删除角色与菜单的关联
            roleMapper.deleteRoleMenus(ids);
            //删除角色与权限的关联
            roleMapper.deleteRolePermissions(ids);
            //删除角色与用户的关联
            roleMapper.deleteRoleUsers(ids);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional(isolation = Isolation.DEFAULT)
    @Override
    public int deleteRoles(List<Integer> ids) {
        try {
            //删除角色
            roleMapper.deleteRoles(ids);
            //删除角色与菜单的关联
            roleMapper.deleteRoleMenus(ids);
            //删除角色与权限的关联
            roleMapper.deleteRolePermissions(ids);
            //删除角色与用户的关联
            roleMapper.deleteRoleUsers(ids);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Integer> getMenuIdByRoleId(int roleId) {
        return roleMapper.getMenuIdByRoleId(roleId);
    }

    @Override
    public List<Integer> getPermissionIdByRoleId(int roleId) {
        return roleMapper.getPermissionIdByRoleId(roleId);
    }


    @Override
    public int bindPermission(int roleId, int permissionId) {
        return roleMapper.bindPermission(roleId,permissionId);
    }

    @Override
    public int bindPermissions(int roleId, List<Integer> permissionIds) {
        return roleMapper.bindPermissions(roleId,permissionIds);
    }

    @Override
    public int unbindPermission(int roleId, int permissionId) {
        return roleMapper.unbindPermission(roleId,permissionId);
    }

    @Override
    public int unbindPermissions(int roleId, List<Integer> permissionIds) {
        return roleMapper.unbindPermissions(roleId,permissionIds);
    }

    @Override
    public int bindMenu(int roleId, int menuId) {
        return roleMapper.bindMenu(roleId,menuId);
    }

    @Override
    public int bindMenus(int roleId, List<Integer> menuIds) {
        return roleMapper.bindMenus(roleId,menuIds);
    }

    @Override
    public int unbindMenu(int roleId, int menuId) {
        return roleMapper.unbindMenu(roleId,menuId);
    }

    @Override
    public int unbindMenus(int roleId, List<Integer> menuId) {
        return roleMapper.unbindMenus(roleId,menuId);
    }

    @Override
    public int getMaxRoleId() {
        return roleMapper.getMaxRoleId();
    }


}
