package com.sunhongbing.petadoption.forestage.controller;

import com.sunhongbing.petadoption.config.CommonUserToken;
import com.sunhongbing.petadoption.backstage.entity.LoginParam;
import com.sunhongbing.petadoption.forestage.entity.SignUpParam;
import com.sunhongbing.petadoption.forestage.service.SignUpService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @className: LoginController
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-16 17:13
 */
@Controller("forestageLoginController")
public class LoginController {

    //logger
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/login")
    public String login() {
        return "forestage/commonUserLogin";
    }

    /**
     * 普通用户登录
     */
    @PostMapping("/login")
    public String login(LoginParam param, Model model) {
        String msg = "";
        // 收集主题的主体和证书
        UsernamePasswordToken token = new CommonUserToken(param.getUsername(), param.getPassword());
        // 提交主体和凭据
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return "redirect:/fore/home";
        } catch (UnknownAccountException | IncorrectCredentialsException e) {
            msg = "账号或密码不正确！";
        } catch (LockedAccountException e) {
            msg = "账号被锁定！请联系管理员。";
        } catch (Exception e) {
            msg = "发生了一些错误！请联系管理员。";
        }
        model.addAttribute("msg", msg);
        return "forestage/commonUserLogin";
    }

}
