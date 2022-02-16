package com.sunhongbing.petadoption.forestage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @className: AdoptionController
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-10 17:33
 */
@Controller
@RequestMapping("/adoption")
public class AdoptionController {

    // adoption-list.html
    @RequestMapping("/list")
    public String list() {
        return "forestage/adoption/adoption-list";
    }

    // adoption-method.html
    @RequestMapping("/method")
    public String method() {
        return "forestage/adoption/adoption-method";
    }

    // happy-adoption.html
    @RequestMapping("/happy")
    public String happy() {
        return "forestage/adoption/happy-adoption";
    }
}