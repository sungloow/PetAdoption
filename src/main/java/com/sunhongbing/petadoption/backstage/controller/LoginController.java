package com.sunhongbing.petadoption.backstage.controller;

import com.sunhongbing.petadoption.backstage.entity.LoginParam;
import com.sunhongbing.petadoption.config.AdminUserToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @className: LoginController
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-16 17:16
 */
@Controller("backstageLoginController")
@RequestMapping("/admin")
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "backstage/html/adminUserLogin";
    }

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public String login(LoginParam param, Model model) {
        UsernamePasswordToken token = new AdminUserToken(param.getUsername(), param.getPassword());
        Subject subject = SecurityUtils.getSubject();
        String msg = "";
        try {
            subject.login(token);
            return "redirect:/admin/index";
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
        return "backstage/html/adminUserLogin";
    }
}
