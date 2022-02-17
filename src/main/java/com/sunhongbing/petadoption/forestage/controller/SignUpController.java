package com.sunhongbing.petadoption.forestage.controller;

import org.springframework.stereotype.Controller;
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
    //sign-up
    @GetMapping("/sign-up")
    public String signUp() {
        return "forestage/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(String username, String password) {
        return "forestage/sign-up";
    }

}
