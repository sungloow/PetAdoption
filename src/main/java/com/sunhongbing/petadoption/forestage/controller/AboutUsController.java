package com.sunhongbing.petadoption.forestage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @className: AboutUsController
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-10 17:43
 */
@Controller
@RequestMapping("/about-us")
public class AboutUsController {

    // introduction.html
    @RequestMapping("/introduction")
    public String index() {
        return "forestage/about-us/introduction";
    }

    // contact.html
    @RequestMapping("/contact")
    public String contact() {
        return "forestage/about-us/contact";
    }

    // working-time.html
    @RequestMapping("/working-time")
    public String workingTime() {
        return "forestage/about-us/working-time";
    }

    // faq.html
    @RequestMapping("/faq")
    public String faq() {
        return "forestage/about-us/faq";
    }
}
