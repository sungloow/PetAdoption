package com.sunhongbing.petadoption.backstage.controller;

import com.github.pagehelper.PageHelper;
import com.sunhongbing.petadoption.backstage.entity.Admin;
import com.sunhongbing.petadoption.backstage.entity.RequestParamsPetList;
import com.sunhongbing.petadoption.backstage.entity.SysRole;
import com.sunhongbing.petadoption.backstage.result.ResultVO;
import com.sunhongbing.petadoption.backstage.service.AdminManageService;
import com.sunhongbing.petadoption.backstage.service.RoleService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @className: AdminManageController
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-03-29 13:25
 */
@Controller
@RequestMapping("/admin-manage")
public class AdminManageController {
    @Autowired
    private AdminManageService adminManageService;
    @Autowired
    private RoleService roleService;

    //staff/list_query
    @GetMapping("/staff/list_query")
    @ResponseBody
    @RequiresPermissions(value = {"root", "staff:manage"}, logical = Logical.OR)
    public Map<String, Object> staffList_query(RequestParamsPetList params) {
        int status = params.getSearch_status();
        PageHelper.startPage(params.getPageNumber(),params.getPageSize());
        List<Admin> adminList = adminManageService.queryAllUsers(status, params.getSort(),params.getOrder());
        List<Admin> adminList_size = adminManageService.queryAllUsers(status, params.getSort(),params.getOrder());
        int total = adminList_size.size();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", total);
        hashMap.put("rows", adminList);
        return hashMap;
    }
    //staff/add
    @GetMapping("/staff/add")
    @RequiresPermissions(value = {"root", "staff:modify"}, logical = Logical.OR)
    public String userAdd(Model model) {
        //查询所有角色
        List<SysRole> roleList = roleService.getAllRole("id", "asc");
        model.addAttribute("roleList", roleList);
        return "backstage/html/menu/user-add";
    }
    //staff/add post
    @PostMapping("/staff/add")
    @ResponseBody
    @RequiresPermissions(value = {"root", "staff:modify"}, logical = Logical.OR)
    public ResultVO userAdd_post(Admin admin, @RequestParam(value = "role",required = false) List<Integer> roleIds) {
        ResultVO resultVO = new ResultVO();
        int addUser = adminManageService.addUser(admin, roleIds);
        if (addUser == 1) {
            resultVO.setCode(200);
            resultVO.setMsg("添加成功");
        } else if (addUser == -1){
            resultVO.setCode(500);
            resultVO.setMsg("用户名只能是字母、数字、下划线组成");
        } else if (addUser == -2) {
            resultVO.setCode(500);
            resultVO.setMsg("用户名已被占用");
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("发生了错误,请联系管理员");
        }
        return resultVO;
    }
    //staff/edit
    @GetMapping("/staff/edit/{id}")
    @RequiresPermissions(value = {"root", "staff:modify"}, logical = Logical.OR)
    public String userEdit(@PathVariable("id") Integer id, Model model) {
        Admin admin = adminManageService.queryUserById(id);
        //查询所有角色
        List<SysRole> roleListAll = roleService.getAllRole("id", "asc");
        //查询用户所有角色
        List<SysRole> roleList = roleService.getRoleListById(id);
        //提取出用户所有角色的id
        List<Integer> roleIds = new ArrayList<>();
        for (SysRole role : roleList) {
            roleIds.add(role.getId());
        }
        model.addAttribute("admin", admin);
        model.addAttribute("roleListAll", roleListAll);
        model.addAttribute("userRoleIds", roleIds);
        return "backstage/html/menu/user-edit";
    }
    //staff/edit post
    @PostMapping("/staff/edit")
    @ResponseBody
    @RequiresPermissions(value = {"root", "staff:modify"}, logical = Logical.OR)
    public ResultVO userEdit(Admin admin, @RequestParam(value = "role",required = false) List<Integer> roleIds) {
        ResultVO resultVO = new ResultVO();
        int modifyUser = adminManageService.modifyUser(admin, roleIds);
        if (modifyUser == 1) {
            resultVO.setCode(200);
            resultVO.setMsg("修改成功");
        } else if (modifyUser == -1) {
            resultVO.setCode(500);
            resultVO.setMsg("用户名只能是字母、数字、下划线组成");
        } else if (modifyUser == -2) {
            resultVO.setCode(500);
            resultVO.setMsg("用户名已被占用");
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("发生了错误,请联系管理员");
        }
        return resultVO;
    }
    //staff/disable
    @PostMapping("/staff/disable")
    @ResponseBody
    @RequiresPermissions(value = {"root", "staff:modify"}, logical = Logical.OR)
    public ResultVO userBlock(@RequestParam List<Integer> ids) {
        ResultVO resultVO = new ResultVO();
        int blockUser = adminManageService.disableUser(ids);
        if (blockUser == ids.size()) {
            resultVO.setCode(200);
            resultVO.setMsg("禁用成功");
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("封禁失败");
        }
        return resultVO;
    }
    //staff/enable
    @PostMapping("/staff/enable")
    @ResponseBody
    @RequiresPermissions(value = {"root", "staff:modify"}, logical = Logical.OR)
    public ResultVO userEnable(@RequestParam List<Integer> ids) {
        ResultVO resultVO = new ResultVO();
        int enableUser = adminManageService.enableUser(ids);
        if (enableUser == ids.size()) {
            resultVO.setCode(200);
            resultVO.setMsg("启用成功");
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("启用失败");
        }
        return resultVO;
    }
    //staff/delete
    @PostMapping("/staff/delete")
    @ResponseBody
    @RequiresPermissions(value = {"root", "staff:del"}, logical = Logical.OR)
    public ResultVO userDelete(@RequestParam List<Integer> ids) {
        ResultVO resultVO = new ResultVO();
        int deleteUserByIds = adminManageService.deleteUserByIds(ids);
        if (deleteUserByIds == ids.size()) {
            resultVO.setCode(200);
            resultVO.setMsg("删除成功");
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("删除失败");
        }
        return resultVO;
    }

    //reset password
    @PostMapping("/staff/reset")
    @ResponseBody
    @RequiresPermissions(value = {"root", "staff:modify"}, logical = Logical.OR)
    public ResultVO resetPassword(@RequestParam List<Integer> ids) {
        ResultVO resultVO = new ResultVO();
        int resetPassword = adminManageService.resetPassword(ids);
        if (resetPassword == ids.size()) {
            resultVO.setCode(200);
            resultVO.setMsg("重置成功");
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("重置失败");
        }
        return resultVO;
    }

    //staff/detail
    @GetMapping("/staff/detail/{id}")
    @RequiresPermissions(value = {"root", "staff:manage"}, logical = Logical.OR)
    public String userDetail(@PathVariable int id, Model model) {
        //查询用户
        Admin admin = adminManageService.queryUserById(id);
        //查询用户所有角色
        List<SysRole> roleList = roleService.getRoleListById(id);
        model.addAttribute("admin", admin);
        model.addAttribute("roleList", roleList);
        return "backstage/html/menu/user-detail";
    }

}
