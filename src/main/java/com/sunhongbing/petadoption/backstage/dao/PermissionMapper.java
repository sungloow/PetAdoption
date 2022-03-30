package com.sunhongbing.petadoption.backstage.dao;

import com.sunhongbing.petadoption.backstage.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionMapper {
    //根据角色id获取权限
    @Select("SELECT * FROM permission WHERE id IN (SELECT permission_id FROM role_permission_ref WHERE role_id = #{roleId})")
    List<SysPermission> getPermissionListByRoleId(int roleId);

    //获取所有权限
    @Select("SELECT * FROM permission where permission != 'root' and permission != 'user:all' order by permission")
    List<SysPermission> getAllPermission();
}
