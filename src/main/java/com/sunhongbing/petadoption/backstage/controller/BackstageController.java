package com.sunhongbing.petadoption.backstage.controller;

import com.sunhongbing.petadoption.backstage.entity.AdminInfo;
import com.sunhongbing.petadoption.backstage.entity.LoginParam;
import com.sunhongbing.petadoption.backstage.entity.SysMenu;
import com.sunhongbing.petadoption.backstage.entity.SysPermission;
import com.sunhongbing.petadoption.backstage.service.MenuService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @className: BackstageController
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-13 14:04
 */
@Controller
@RequestMapping("/admin")
public class BackstageController {

    @Autowired
    private MenuService menuService;

    //登录页面
    @GetMapping("/login")
    public String login() {
        return "backstage/html/login";
    }

    //登录提交请求
    @PostMapping("/login")
    public String login_auth(LoginParam loginParam, Model model) {
        //验证用户名和密码
        org.apache.shiro.subject.Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginParam.getUsername(), loginParam.getPassword());
        String msg = "";
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            msg = "账号不存在！";
        } catch (IncorrectCredentialsException e) {
            msg = "密码不正确！";
        } catch (LockedAccountException e) {
            msg = "账号被锁定！请联系管理员。";
        } catch (Exception e) {
            msg = "发生了一些错误！请联系管理员。";
        }
        model.addAttribute("msg", msg);
        return "backstage/html/login";
    }

    // 后台首页
    @GetMapping("/index")
    public String index() {
        return "backstage/html/index";
    }

    // 仪表盘
    @GetMapping("/home")
    @RequiresPermissions("admin:home")
    public String home() {
        return "backstage/html/menu/home";
    }

    //用户管理
    @GetMapping("/user")
    @RequiresPermissions("user:list")
    public String user() {
        return "backstage/html/menu/user";
    }

    // 403
    @GetMapping("/unAuth")
    public String error403() {
        return "backstage/html/error/403";
    }

    // listAdminMenu
    @GetMapping("/listAdminMenu")
    @ResponseBody
    public List<SysMenu> listAdminMenu() {
        //1、判断当前用户的角色
        Subject subject = SecurityUtils.getSubject();

        //2、根据角色查询菜单
        List<SysMenu> menuListByRole = null;
        if(subject.hasRole("root")) {
            //超级管理员
            menuListByRole = menuService.getMenuListByRole("root");
        } else if (subject.hasRole("staff")) {
            //staff
            menuListByRole = menuService.getMenuListByRole("staff");
        }
        return menuListByRole;
    }
}
