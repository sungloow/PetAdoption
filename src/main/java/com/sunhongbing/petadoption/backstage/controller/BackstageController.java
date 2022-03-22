package com.sunhongbing.petadoption.backstage.controller;

import com.sunhongbing.petadoption.backstage.entity.Animal;
import com.sunhongbing.petadoption.backstage.service.PetManageService;
import com.sunhongbing.petadoption.config.AdminUserToken;
import com.sunhongbing.petadoption.backstage.entity.LoginParam;
import com.sunhongbing.petadoption.backstage.entity.SysMenu;
import com.sunhongbing.petadoption.backstage.result.ResultVO;
import com.sunhongbing.petadoption.backstage.service.MenuService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @className: BackstageController
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-13 14:04
 */
@Controller
@RequestMapping("/admin")
public class BackstageController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private PetManageService petManageService;


//    //登录页面
//    @GetMapping("/login")
//    public String login() {
//        return "backstage/html/login";
//    }
//
//    //登录提交请求
//    @PostMapping("/login")
//    public String login_auth(LoginParam loginParam, Model model) {
//        //验证用户名和密码
//        org.apache.shiro.subject.Subject subject = SecurityUtils.getSubject();
//        UsernamePasswordToken token = new UsernamePasswordToken(loginParam.getUsername(), loginParam.getPassword());
//        String msg = "";
//        try {
//            subject.login(token);
//        } catch (UnknownAccountException e) {
//            msg = "账号不存在！";
//        } catch (IncorrectCredentialsException e) {
//            msg = "密码不正确！";
//        } catch (LockedAccountException e) {
//            msg = "账号被锁定！请联系管理员。";
//        } catch (Exception e) {
//            msg = "发生了一些错误！请联系管理员。";
//        }
//        model.addAttribute("msg", msg);
//        return "backstage/html/login";
//    }


    // 后台首页
    @GetMapping("/index")
    public String index() {
        return "backstage/html/index";
    }

    // 仪表盘
    @GetMapping("/dashboard")
    @RequiresPermissions("admin:dashboard")
    public String home() {
        return "backstage/html/menu/home";
    }

    //人员管理
    @GetMapping("/staff")
    @RequiresPermissions("staff:list")
    public String user() {
        return "backstage/html/menu/user";
    }

    //角色管理
    @GetMapping("/role")
    @RequiresPermissions("role:list")
    public String role() {
        return "backstage/html/menu/role";
    }

    //文章管理
    @GetMapping("/article")
    @RequiresPermissions("article:all")
    public String article() {
        return "backstage/html/menu/article";
    }

    //宠物管理
    @GetMapping("/pet")
    @RequiresPermissions("pet:all")
    public String pet(Model model) throws ParseException {
        List<Animal> animalList = petManageService.findAll(-99);
        model.addAttribute("animalList", animalList);
        return "backstage/html/menu/pet-list";
    }
    //查看宠物信息
    @GetMapping("/pet/list")
    @RequiresPermissions("pet:all")
    public String petInfo(Model model) {
        //查询所有宠物信息
        List<Animal> animalList = petManageService.findAll(-99);
        model.addAttribute("animalList", animalList);
        return "backstage/html/menu/pet-list";
    }
    //添加宠物
    @GetMapping("/pet/add")
    @RequiresPermissions("pet:all")
    public String petAdd() {
        return "backstage/html/menu/pet-add";
    }
    @PostMapping("/pet/add")
    @RequiresPermissions("pet:all")
    public String petAdd_post(Animal animal, Model model) {
        int i = petManageService.insertPet(animal);
        if (i > 0) {
            model.addAttribute("msg_flag", "ok");
            model.addAttribute("msg", "添加成功！");
        } else if (i == -1) {
            model.addAttribute("msg_flag", "fail");
            model.addAttribute("msg", "添加失败，数据有误！");
        }
        else {
            model.addAttribute("msg_flag", "fail");
            model.addAttribute("msg", "添加失败！");
        }
        return "backstage/html/menu/pet-add";
    }
    //修改宠物信息
    @GetMapping("/pet/edit/{id}")
    @RequiresPermissions("pet:all")
    public String petEdit(@PathVariable("id") Integer id, Model model) {
        Animal animal = petManageService.findPetById(String.valueOf(id));
        model.addAttribute("animal", animal);
        return "backstage/html/menu/pet-edit";
    }
    @PostMapping("/pet/edit")
    @RequiresPermissions("pet:all")
    public String petEdit(Animal animal, Model model ) {
        int re = petManageService.updatePet(animal);
        System.out.println("修改结果：" + re);
        System.out.println(animal);
        if (re == 1) {
            model.addAttribute("msg_flag", "ok");
            model.addAttribute("msg", "修改成功！");
        } else {
            model.addAttribute("msg_flag", "fail");
            model.addAttribute("msg", "修改失败！");
        }
        return "backstage/html/menu/pet-edit";
    }
    //删除宠物
    @PostMapping("/pet/delete")
    @ResponseBody
    @RequiresPermissions("pet:all")
    public ResultVO petDelete(String id) {
        ResultVO resultVO = new ResultVO();
        if (id == null || id.equals("")) {
            resultVO.setCode(500);
            resultVO.setMsg("删除失败，数据有误！");
            return resultVO;
        }
        int re = petManageService.deletePet(id);
        if (re == 1) {
            resultVO.setCode(200);
            resultVO.setMsg("删除成功！");
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("删除失败！");
        }
        return resultVO;
    }

    //志愿者管理
    @GetMapping("/volunteer")
    @RequiresPermissions("volunteer:all")
    public String volunteer() {
        return "backstage/html/menu/volunteer";
    }
    //查看志愿者
    @GetMapping("/volunteer/list")
    @RequiresPermissions("volunteer:all")
    public String volunteerInfo() {
        return "backstage/html/menu/volunteer";
    }
    //添加志愿者
    @GetMapping("/volunteer/add")
    @RequiresPermissions("volunteer:all")
    public String volunteerAdd() {
        return "backstage/html/menu/volunteer-add";
    }

    //审批
    @GetMapping("/approval")
    @RequiresPermissions("approval:all")
    public String approval() {
        return "backstage/html/menu/approval";
    }
    //宠物领养审批
    @GetMapping("/approval/pet")
    @RequiresPermissions("approval:all")
    public String approvalPet() {
        return "backstage/html/menu/approval-pet";
    }
    //志愿者申请审批
    @GetMapping("/approval/volunteer")
    @RequiresPermissions("approval:all")
    public String approvalVolunteer() {
        return "backstage/html/menu/approval-volunteer";
    }
    //文章审核
    @GetMapping("/approval/article")
    @RequiresPermissions("approval:all")
    public String approvalArticle() {
        return "backstage/html/menu/approval-article";
    }

    //新闻
    @GetMapping("/article/news-list")
    @RequiresPermissions("article:all")
    public String news() {
        return "backstage/html/menu/news";
    }
    //活动
    @GetMapping("/article/activity")
    @RequiresPermissions("article:all")
    public String activity() {
        return "backstage/html/menu/activity";
    }
    //科学喂养
    @GetMapping("/article/feed")
    @RequiresPermissions("article:all")
    public String science() {
        return "backstage/html/menu/feed";
    }


    // 403
    @GetMapping("/unAuth")
    public String error403() {
        return "error/403";
    }

    // listMenu
    @GetMapping("/listMenu")
    @ResponseBody
    public ResultVO listAdminMenu() {
        //1、判断当前用户的角色
        Subject subject = SecurityUtils.getSubject();
        //2、根据角色查询菜单
        List<SysMenu> result = new ArrayList<>();
        if(subject.hasRole("root")) {
            //超级管理员
            result = menuService.getMenuListByRole("root");
        } else if (subject.hasRole("article")) {
            //article
            result = menuService.getMenuListByRole("article");
        } else if (subject.hasRole("volunteer")) {
            //volunteer
            result = menuService.getMenuListByRole("volunteer");
        } else if (subject.hasRole("pet")) {
            //pet
            result = menuService.getMenuListByRole("pet");
        } else if (subject.hasRole("approval")) {
            //approval
            result = menuService.getMenuListByRole("approval");
        }
        if(result == null || result.size() == 0) {
            return ResultVO.error("菜单为空！");
        }
        return new ResultVO(200, "菜单查询成功！", result);
    }
}
