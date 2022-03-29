package com.sunhongbing.petadoption.backstage.service;

import com.sunhongbing.petadoption.backstage.entity.SysMenu;
import com.sunhongbing.petadoption.backstage.entity.SysPermission;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MenuService {
    List<SysMenu> getMenuListByRole(String role);

    List<SysMenu> getMenuListByRoles(String[] roles);

    //获取所有菜单列表
    List<SysMenu> getAllMenuList();

}
