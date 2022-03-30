package com.sunhongbing.petadoption.forestage.controller;

import com.sunhongbing.petadoption.forestage.entity.SignUpParam;
import com.sunhongbing.petadoption.forestage.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @className: SignUpController
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-17 10:50
 */
@Controller
public class SignUpController {
    @Autowired
    private SignUpService signUpService;
    //sign-up
    @GetMapping("/sign-up")
    public String signUp() {
        return "forestage/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(SignUpParam signUpParam, Model model) {
        //打印参数
//        System.out.println(signUpParam);
        //验证参数
        if (!signUpService.validateParam(signUpParam)) {
            model.addAttribute("signUpResult", "信息有错误！");
        } else if (!signUpService.checkUserExist(signUpParam.getEmail())) {
            //检查用户是否存在
            model.addAttribute("signUpResult", "邮箱已注册！");
        } else {
            //调用service
            int result = signUpService.register(signUpParam);
            if (result == 1) {
                model.addAttribute("signUpResult", "注册成功，即将跳转至登录页面！");
                model.addAttribute("signUpResultFlag", "ok");
            } else {
                model.addAttribute("signUpResult", "注册失败！");
            }
        }

        return "forestage/sign-up";
    }

}
