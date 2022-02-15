package com.sunhongbing.petadoption.backstage.dao;

import com.sunhongbing.petadoption.backstage.entity.SysPermission;
import com.sunhongbing.petadoption.backstage.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper {

    // 根据用户id查询角色
    @Select("SELECT * FROM role WHERE id IN (SELECT role_id FROM role_admin_ref WHERE admin_id = #{id})")
    List<SysRole> getRoleListByAdminId(String id);

}
