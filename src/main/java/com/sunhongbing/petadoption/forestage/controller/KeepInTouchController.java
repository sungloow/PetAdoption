package com.sunhongbing.petadoption.forestage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @className: KeepInTouchController
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-10 17:41
 */
@Controller
@RequestMapping("/keep-in-touch")
public class KeepInTouchController {

    // news.html
    @RequestMapping("/news")
    public String index() {
        return "forestage/keep-in-touch/news";
    }

    // activity.html
    @RequestMapping("/activity")
    public String activity() {
        return "forestage/keep-in-touch/activity";
    }
}
