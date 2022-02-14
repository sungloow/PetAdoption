package com.sunhongbing.petadoption.backstage.dao;

import com.sunhongbing.petadoption.backstage.entity.SysMenu;
import com.sunhongbing.petadoption.backstage.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MenuMapper {
    //根据角色获取菜单列表
    @Select("SELECT * FROM permission WHERE id IN \n" +
            "    (SELECT menu_id FROM `role_menu_ref` WHERE role_id IN \n" +
            "    (SELECT id FROM role WHERE role=#{role}))")
    List<SysMenu> getMenuListByRole(String role);
}
