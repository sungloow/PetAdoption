package com.sunhongbing.petadoption.backstage.dao;

import com.sunhongbing.petadoption.backstage.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MenuMapper {
    //根据角色获取菜单列表
    @Select("SELECT * FROM menu WHERE id IN \n" +
            "(SELECT menu_id FROM role_menu_ref WHERE role_id IN\n" +
            "(SELECT id FROM role WHERE role= #{role})) ORDER BY id")
    List<SysMenu> getMenuListByRole(String role);

    //获取所有菜单列表
    @Select("SELECT * FROM menu where type != 'root_menu' ORDER BY id")
    List<SysMenu> getAllMenuList();
}
