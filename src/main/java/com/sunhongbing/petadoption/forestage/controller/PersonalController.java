package com.sunhongbing.petadoption.forestage.controller;

import com.sunhongbing.petadoption.backstage.entity.User;
import com.sunhongbing.petadoption.backstage.result.ResultVO;
import com.sunhongbing.petadoption.forestage.entity.ApplyAnimal;
import com.sunhongbing.petadoption.forestage.entity.EditParam;
import com.sunhongbing.petadoption.forestage.entity.EditPasswordParam;
import com.sunhongbing.petadoption.forestage.service.PersonalService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @className: PersonalController
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-04-01 17:06
 */
@Controller
@RequiresPermissions("user:all")
@RequestMapping("/personal")
public class PersonalController {

    @Autowired
    private PersonalService personalService;

    //home
    @GetMapping("/home")
    public String home(Model model) {
        int userId = (int) SecurityUtils.getSubject().getPrincipal();
        User user = personalService.queryUserInfoById(userId);
        model.addAttribute("user", user);
        return "forestage/home";
    }

    //persom_info
    @GetMapping("/personal_info")
    public String personal_info(Model model) {
        int userId = (int) SecurityUtils.getSubject().getPrincipal();
        User user = personalService.queryUserInfoById(userId);
        model.addAttribute("user", user);
        return "forestage/person-info";
    }

    //personal_info_edit
    @GetMapping("/personal_info_edit")
    public String personal_info_edit(Model model) {
        int userId = (int) SecurityUtils.getSubject().getPrincipal();
        User user = personalService.queryUserInfoById(userId);
        model.addAttribute("user", user);
        return "forestage/personal-info-edit";
    }
    //personal_info_edit post
    @PostMapping("/personal_info_edit")
    @ResponseBody
    public ResultVO personal_info_edit_post(EditParam param) {
        ResultVO vo = new ResultVO();
        try {
            int userId = (int) SecurityUtils.getSubject().getPrincipal();
            param.setId(userId);
            int i = personalService.updateUserInfo(param);
            if (i == 1) {
                vo.setCode(200);
                vo.setMsg("修改成功");
            } else if (i == -1) {
                vo.setCode(500);
                vo.setMsg("数据有误");
            } else {
                vo.setCode(500);
                vo.setMsg("修改失败");
            }
        } catch (Exception e) {
            vo.setCode(500);
            vo.setMsg("修改失败");
        }
        return vo;
    }
    //ApplyAnimalList
    @GetMapping("/apply_list")
    public String apply_list(Model model) {
        int userId = (int) SecurityUtils.getSubject().getPrincipal();
        List<ApplyAnimal> applyAnimalList = personalService.queryAdoptList(userId);
        model.addAttribute("animalList", applyAnimalList);
        return "forestage/apply-list";
    }

    //editPwd
    @GetMapping("/editPwd")
    public String editPwd() {
        return "forestage/edit-password";
    }
    //editPwd post
    @PostMapping("/editPwd")
    @ResponseBody
    public ResultVO editPwd_post(EditPasswordParam param) {
        ResultVO vo = new ResultVO();
        try {
            int userId = (int) SecurityUtils.getSubject().getPrincipal();
            param.setId(userId);
            //查询用户信息
            String oldPwd = personalService.getOldPwd(userId);
            //判断原密码是否正确
            if (!DigestUtils.md5DigestAsHex(param.getOldPwd().getBytes()).equals(oldPwd)) {
                vo.setCode(500);
                vo.setMsg("原密码错误");
                return vo;
            }
            int i = personalService.updateUserPwd(param);
            if (i == 1) {
                vo.setCode(200);
                vo.setMsg("密码修改成功");
            } else {
                vo.setCode(500);
                vo.setMsg("修改失败");
            }
            return vo;
        } catch (Exception e) {
            vo.setCode(500);
            vo.setMsg("修改失败");
        }
        return vo;
    }
}
