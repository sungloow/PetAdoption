package com.sunhongbing.petadoption.forestage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 孙红兵
 * @className ForestageController
 * @description TODO
 * @studentId 202018113175
 * @date 2021/12/12 21:20
 */
@Controller
public class ForestageController {
    //logback
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 测试访问首页
    @GetMapping("/")
    public String index() {
        logger.info("访问首页");
        return "forestage/index";
    }
}
