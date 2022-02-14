package com.sunhongbing.petadoption.forestage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @className: VolunteerController
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-10 17:39
 */
@Controller
@RequestMapping("/volunteer")
public class VolunteerController {

    // foster-information.html
    @RequestMapping("/foster")
    public String index() {
        return "forestage/volunteer/foster-information";
    }

    // volunteer-information.html
    @RequestMapping("/information")
    public String volunteer() {
        return "forestage/volunteer/volunteer-information";
    }
}
