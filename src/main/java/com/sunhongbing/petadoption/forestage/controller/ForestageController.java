package com.sunhongbing.petadoption.forestage.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 孙红兵
 * @className ForestageController
 * @description TODO
 * @studentId 202018113175
 * @date 2021/12/12 21:20
 */
@Controller
@RequestMapping("/fore")
public class ForestageController {
    //
    //logback
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 访问首页
    @GetMapping("/")
    public String index2() {
        logger.info("访问首页");
        return "forestage/index";
    }

    //home
    @GetMapping("/home")
    @RequiresPermissions("user:home")
    public String home() {
        return "forestage/home";
    }

    // other
    //shop.html
    @GetMapping("/shop")
    public String shop() {
        return "forestage/other/shop";
    }
    //shop-details.html
    @GetMapping("/shop-details")
    public String shopDetails() {
        return "forestage/other/shop-details";
    }
    // breeder.html
    @GetMapping("/breeder")
    public String breeder() {
        return "forestage/other/breeder";
    }
    // breeder-details.html
    @GetMapping("/breeder-details")
    public String breederDetails() {
        return "forestage/other/breeder-details";
    }
}
