package com.sunhongbing.petadoption.backstage.dao;

import com.sunhongbing.petadoption.backstage.entity.SysPermission;
import com.sunhongbing.petadoption.backstage.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper {

    @Select("SELECT * FROM role WHERE id IN (SELECT role_id FROM role_admin_ref WHERE admin_id = #{id})")
    List<SysRole> getRoleListByAdminId(String id);

    //根据角色id获取权限
    @Select("SELECT * FROM permission WHERE id IN (SELECT permission_id FROM role_permission_ref WHERE role_id = #{roleId})")
    List<SysPermission> getPermissionListByRoleId(int roleId);
}
