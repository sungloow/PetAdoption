package com.sunhongbing.petadoption.backstage.service.impl;

import com.sunhongbing.petadoption.backstage.dao.MenuMapper;
import com.sunhongbing.petadoption.backstage.entity.SysMenu;
import com.sunhongbing.petadoption.backstage.entity.SysPermission;
import com.sunhongbing.petadoption.backstage.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return menuMapper.getMenuListByRole(role);
    }
}
