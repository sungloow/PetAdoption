package com.sunhongbing.petadoption.backstage.controller;

import com.github.pagehelper.PageHelper;
import com.sunhongbing.petadoption.backstage.entity.*;
import com.sunhongbing.petadoption.backstage.enums.ApplyStatus;
import com.sunhongbing.petadoption.backstage.service.*;
import com.sunhongbing.petadoption.backstage.result.ResultVO;
import com.sunhongbing.petadoption.forestage.entity.ApplyRecord;
import com.sunhongbing.petadoption.forestage.service.AdoptionService;
import com.sunhongbing.petadoption.forestage.service.PersonalService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    @Autowired
    private AdoptionService adoptionService;
    @Autowired
    private PersonalService personalService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AdminManageService adminManageService;


    // 后台首页
    @GetMapping("/index")
    public String index(Model model) {
        // 获取当前用户
        Subject subject = SecurityUtils.getSubject();
        int id = (int) subject.getPrincipal();
        Admin admin = adminManageService.queryUserById(id);
        model.addAttribute("admin", admin);
        return "backstage/html/index";
    }

    // 后台欢迎页
    @GetMapping("/dashboard")
    public String home(Model model) {
        // 获取当前用户
        try {
            Subject subject = SecurityUtils.getSubject();
            int id = (int) subject.getPrincipal();
            Admin admin = adminManageService.queryUserById(id);
            model.addAttribute("admin", admin);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "backstage/html/menu/home";
    }


    //modify password
    @RequestMapping("/modify-password")
    @RequiresPermissions(value = {"root", "modify:password"}, logical = Logical.OR)
    public String modifyPassword() {
        return "backstage/html/menu/edit-password";
    }



    //人员管理
    @GetMapping("/staff")
    @RequiresPermissions(value = {"root", "staff:query"}, logical = Logical.OR)
    public String user() {
        return "backstage/html/menu/user";
    }


    //角色管理
    @GetMapping("/role")
    @RequiresPermissions(value = {"root", "role:query"}, logical = Logical.OR)
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
    @RequiresPermissions("pet:query")
    public String pet() {
        return "backstage/html/menu/pet-list2";
    }

    //feedback管理
    @GetMapping("/feedback")
    @RequiresPermissions("feedback:all")
    public String feedback() {
        return "backstage/html/menu/feedback";
    }

    /*
     宠物领养审批 begin
     */
    //宠物领养审批
    @GetMapping("/approval/pet")
    @RequiresPermissions("approval:pet")
    public String approvalPet() {
        return "backstage/html/menu/approval-pet";
    }
    @GetMapping("/approval/pet_list")
    @ResponseBody
    @RequiresPermissions("approval:pet")
    public Map<String, Object> approvalPetList(RequestParamsPetList params) {
        int status = params.getSearch_status();
        PageHelper.startPage(params.getPageNumber(),params.getPageSize());
        List<ApplyRecord> applyRecordList = adoptionService.getApplyListByStatus(status, params.getSort(), params.getOrder());
        List<ApplyRecord> applyRecordList_size = adoptionService.getApplyListByStatus(ApplyStatus.ALL.getCode(), params.getSort(), params.getOrder());
        int total = applyRecordList_size.size();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", total);
        hashMap.put("rows", applyRecordList);
        return hashMap;
    }
    //审批页面
    @GetMapping("/approval/web/{petId}/{userId}")
    @RequiresPermissions("approval:pet")
    public String approvalWeb(@PathVariable("petId") Integer petId, @PathVariable("userId") Integer userId, Model model) {
        Animal animal = petManageService.findPetById(petId);
        User user = personalService.queryUserInfoById(userId);
        model.addAttribute("animal", animal);
        model.addAttribute("user", user);
        return "backstage/html/menu/approval-pet-web";
    }
    @PostMapping("/approval/edit")
    @ResponseBody
    @RequiresPermissions("approval:pet")
    public ResultVO approvalEdit(ApplyRecord apply) {
        ResultVO resultVO = new ResultVO();
        int re = 0;
        if (apply.getStatus() == ApplyStatus.PASS.getCode()) {
            re = adoptionService.accept(apply.getUserId(), apply.getPetId());
        } else if (apply.getStatus() == ApplyStatus.REJECT.getCode()) {
            re = adoptionService.reject(apply.getUserId(), apply.getPetId());
        }
        if (re == 1) {
            resultVO.setCode(200);
            resultVO.setMsg("success！");
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("fail！");
        }
        return resultVO;
    }
    //查看用户信息
    @GetMapping("/user/info/{id}")
    @RequiresPermissions("approval:pet")
    public String userInfo(@PathVariable("id") Integer id, Model model) {
        User user = personalService.queryUserInfoById(id);
        model.addAttribute("user", user);
        return "backstage/html/menu/fore-user-info";
    }
    /*
     宠物领养审批 end
     */


//文章审核
    @GetMapping("/approval/article")
    @RequiresPermissions("approval:article")
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
    //快乐领养
    @GetMapping("/article/happy-adoption")
    @RequiresPermissions("article:all")
    public String adoption() {
        return "backstage/html/menu/happy-adoption-article";
    }
    // article/banner
    @GetMapping("/article/banner")
    @RequiresPermissions("article:all")
    public String banner() {
        return "backstage/html/menu/banner";
    }
    // article/about-us
    @GetMapping("/article/about-us")
    @RequiresPermissions("article:all")
    public String aboutUs() {
        return "backstage/html/menu/about-us";
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
        //取出当前用户的id
        int id = (int) subject.getPrincipal();
        List<SysRole> roleList = roleService.getRoleListById(id);
        String[] roles = new String[roleList.size()];
        for (int i = 0; i < roleList.size(); i++) {
            roles[i] = roleList.get(i).getRole();
        }
        result = menuService.getMenuListByRoles(roles);
        if(result == null || result.size() == 0) {
            return ResultVO.error("菜单为空！");
        }
        return new ResultVO(200, "菜单查询成功！", result);
    }
}
