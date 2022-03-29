package com.sunhongbing.petadoption.backstage.service;

import com.sunhongbing.petadoption.backstage.entity.SysRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface RoleService {
    List<SysRole> getRoleListById(int id);

    // get role by id
    SysRole getRoleById(int id);

    // 获取所有角色
    List<SysRole> getAllRole(String order, String sort);

    //添加角色
    int addRole(SysRole role);

    //修改角色的权限和菜单
    int modifyRole(int id, List<Integer> menu_ids, List<Integer> permission_ids);

    //删除角色
    int deleteRole(int id);
    //批量删除角色
    int deleteRoles(List<Integer> ids);


    //根据角色ID查询关联的菜单ID
    List<Integer> getMenuIdByRoleId(int roleId);
    //根据角色ID查询关联的权限ID
    List<Integer> getPermissionIdByRoleId(int roleId);

    //角色绑定权限
    int bindPermission(int roleId, int permissionId);
    //角色批量绑定权限
    int bindPermissions(int roleId, List<Integer> permissionIds);
    //角色解绑权限
    int unbindPermission(int roleId, int permissionId);
    //角色批量解绑权限
    int unbindPermissions(int roleId, List<Integer> permissionIds);


    //角色添加菜单
    int bindMenu(int roleId, int menuId);
    //角色批量添加菜单
    int bindMenus(int roleId, List<Integer> menuIds);
    //角色解绑菜单
    int unbindMenu(int roleId, int menuId);
    //角色批量解绑菜单
    int unbindMenus(int roleId, List<Integer> menuIds);

    int getMaxRoleId();

}
