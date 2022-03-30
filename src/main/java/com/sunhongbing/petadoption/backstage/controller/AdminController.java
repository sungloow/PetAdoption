package com.sunhongbing.petadoption.backstage.controller;

import com.sunhongbing.petadoption.backstage.entity.Admin;
import com.sunhongbing.petadoption.backstage.result.ResultVO;
import com.sunhongbing.petadoption.backstage.service.AdminManageService;
import com.sunhongbing.petadoption.backstage.service.AdminService;
import com.sunhongbing.petadoption.forestage.entity.EditPasswordParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @className: AdminController
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-03-29 13:24
 */
@Controller
@RequestMapping("/back-user")
public class AdminController {

    @Autowired
    private AdminManageService adminManageService;
    @Autowired
    private AdminService adminService;

    // post modify password
    @PostMapping("/modify-password")
    @ResponseBody
    @RequiresPermissions("modify:password")
    public ResultVO editPwd_post(EditPasswordParam param) {
        ResultVO vo = new ResultVO();
        try {
            int userId = (int) SecurityUtils.getSubject().getPrincipal();
            param.setId(userId);
            //查询用户信息
            Admin admin = adminManageService.queryUserById(userId);
            String oldPwd = admin.getPassword();
            //判断原密码是否正确
            if (!DigestUtils.md5DigestAsHex(param.getOldPwd().getBytes()).equals(oldPwd)) {
                vo.setCode(500);
                vo.setMsg("原密码错误");
                return vo;
            }
            int i = adminService.updatePassword(userId, param.getNewPwd());
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
