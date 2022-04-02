package com.sunhongbing.petadoption.backstage.service.impl;

import com.sunhongbing.petadoption.backstage.dao.MenuMapper;
import com.sunhongbing.petadoption.backstage.entity.SysMenu;
import com.sunhongbing.petadoption.backstage.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @className: MenuServiceImpl
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-14 16:56
 */
@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public List<SysMenu> getMenuListByRole(String role) {
        List<SysMenu> menuList;
        List<SysMenu> newMenuList = new ArrayList<>();
        try {
            //1、根据角色获得所有的菜单（包括一级和二级）
            menuList = menuMapper.getMenuListByRole(role);
            for (int i = 0; i < menuList.size(); i++) {
                SysMenu menu = menuList.get(i);
                List<SysMenu> childMenuList = new ArrayList<>();
                //2、拼装二级菜单
                if (menu.getPid() == 0) {
                    for (SysMenu sysMenu : menuList) {
                        if (Objects.equals(menu.getId(), sysMenu.getPid())) {
                            childMenuList.add(sysMenu);
                        }
                    }
                    menu.setChildMenu(childMenuList);
                    newMenuList.add(menu);
                }
            }
        } catch (Exception e) {
            System.out.println("【后台菜单获取失败】，cause: " + e);
        }
        return newMenuList;
    }

    @Override
    public List<SysMenu> getMenuListByRoles(String[] roles) {
        List<SysMenu> menuList;
        List<SysMenu> newMenuList = new ArrayList<>();
        for (String role : roles) {
            try {
                //1、根据角色获得所有的菜单（包括一级和二级）
                menuList = menuMapper.getMenuListByRole(role);
                for (int i = 0; i < menuList.size(); i++) {
                    SysMenu menu = menuList.get(i);
                    List<SysMenu> childMenuList = new ArrayList<>();
                    //2、拼装二级菜单
                    if (menu.getPid() == 0) {
                        for (SysMenu sysMenu : menuList) {
                            if (Objects.equals(menu.getId(), sysMenu.getPid())) {
                                childMenuList.add(sysMenu);
                            }
                        }
                        menu.setChildMenu(childMenuList);
                        newMenuList.add(menu);
                    }
                }
            } catch (Exception e) {
                System.out.println("【后台菜单获取失败】，cause: " + e);
            }
        }
        return newMenuList;
    }

    @Override
    public List<SysMenu> getAllMenuList() {
        List<SysMenu> menuList;
        List<SysMenu> newMenuList = new ArrayList<>();
        try {
            menuList = menuMapper.getAllMenuList();
            for (int i = 0; i < menuList.size(); i++) {
                SysMenu menu = menuList.get(i);
                List<SysMenu> childMenuList = new ArrayList<>();
                //2、拼装二级菜单
                if (menu.getPid() == 0) {
                    for (SysMenu sysMenu : menuList) {
                        if (Objects.equals(menu.getId(), sysMenu.getPid())) {
                            childMenuList.add(sysMenu);
                        }
                    }
                    menu.setChildMenu(childMenuList);
                    newMenuList.add(menu);
                }
            }
        } catch (Exception e) {
            System.out.println("【后台菜单获取失败】，cause: " + e);
        }
        return newMenuList;
    }
}
