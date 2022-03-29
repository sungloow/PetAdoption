package com.sunhongbing.petadoption.backstage.controller;

import com.github.pagehelper.PageHelper;
import com.sunhongbing.petadoption.backstage.entity.*;
import com.sunhongbing.petadoption.backstage.enums.ApplyStatus;
import com.sunhongbing.petadoption.backstage.enums.PetStatus;
import com.sunhongbing.petadoption.backstage.service.*;
import com.sunhongbing.petadoption.backstage.result.ResultVO;
import com.sunhongbing.petadoption.config.OSSUtil;
import com.sunhongbing.petadoption.forestage.entity.ApplyRecord;
import com.sunhongbing.petadoption.forestage.service.AdoptionService;
import com.sunhongbing.petadoption.forestage.service.PersonalService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
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
    private ArticleService articleService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private AdminManageService adminManageService;




    // 后台首页
    @GetMapping("/index")
    public String index() {
        return "backstage/html/index";
    }

    // 仪表盘
    @GetMapping("/dashboard")
    @RequiresPermissions("root")
    public String home() {
        return "backstage/html/menu/home";
    }


    //人员管理
    @GetMapping("/staff")
    @RequiresPermissions(value = {"root", "staff:manage"}, logical = Logical.OR)
    public String user() {
        return "backstage/html/menu/user";
    }
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
    public String userAdd() {
        return "backstage/html/menu/user-add";
    }
    //staff/add post
    @PostMapping("/staff/add")
    @ResponseBody
    @RequiresPermissions(value = {"root", "staff:modify"}, logical = Logical.OR)
    public ResultVO userAdd_post(Admin admin) {
        ResultVO resultVO = new ResultVO();
        int addUser = adminManageService.addUser(admin);
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
        model.addAttribute("admin", admin);
        return "backstage/html/menu/user-edit";
    }
    //staff/edit post
    @PostMapping("/staff/edit")
    @ResponseBody
    @RequiresPermissions(value = {"root", "staff:modify"}, logical = Logical.OR)
    public ResultVO userEdit(Admin admin) {
        ResultVO resultVO = new ResultVO();
        int modifyUser = adminManageService.modifyUser(admin);
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



    //角色管理
    @GetMapping("/role")
    @RequiresPermissions(value = {"root", "role:manage"}, logical = Logical.OR)
    public String role() {
        return "backstage/html/menu/role";
    }
    @GetMapping("/role/list_query")
    @ResponseBody
    @RequiresPermissions(value = {"root", "role:manage"}, logical = Logical.OR)
    public Map<String, Object> roleList_query(RequestParamsPetList params) {
        PageHelper.startPage(params.getPageNumber(),params.getPageSize());
        List<SysRole> roleList = roleService.getAllRole(params.getSort(),params.getOrder());
        List<SysRole> roleList_size = roleService.getAllRole(params.getSort(),params.getOrder());
        int total = roleList_size.size();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", total);
        hashMap.put("rows", roleList);
        return hashMap;
    }
    @GetMapping("/role/detail/{id}")
    @RequiresPermissions(value = {"root", "role:manage"}, logical = Logical.OR)
    public String roleDetail(Model model, @PathVariable int id) {
        SysRole role = roleService.getRoleById(id);
        //获取角色对应的菜单
        List<SysMenu> menuListByRole = menuService.getMenuListByRole(role.getRole());
        //获取角色对应的权限
        List<SysPermission> permissionListByRole = permissionService.getPermissionListByRoleId(id);

        model.addAttribute("role", role);
        model.addAttribute("menuList", menuListByRole);
        model.addAttribute("permissionList", permissionListByRole);
        return "backstage/html/menu/role-detail";
    }
    //role modify
    @GetMapping("/role/modify/{id}")
    @RequiresPermissions(value = {"root", "role:manage"}, logical = Logical.OR)
    public String roleModify(Model model, @PathVariable int id) {
        SysRole role = roleService.getRoleById(id);
        //获取角色对应的菜单
        List<SysMenu> menuListByRole = menuService.getMenuListByRole(role.getRole());
        //取出id
        List<Integer> menuIdList = new ArrayList<>();
        for (SysMenu menu : menuListByRole) {
            menuIdList.add(menu.getId());
            if (menu.getChildMenu() != null) {
                for (SysMenu childMenu : menu.getChildMenu()) {
                    menuIdList.add(childMenu.getId());
                }
            }
        }
        //获取角色对应的权限
        List<SysPermission> permissionListByRole = permissionService.getPermissionListByRoleId(id);
        //取出id
        List<Integer> permissionIdList = new ArrayList<>();
        for (SysPermission permission : permissionListByRole) {
            permissionIdList.add(permission.getId());
        }

        //获取所有菜单
        List<SysMenu> menuList = menuService.getAllMenuList();
        //获取所有权限
        List<SysPermission> permissionList = permissionService.getAllPermission();
        model.addAttribute("role", role);
        model.addAttribute("menuList", menuList);
        model.addAttribute("permissionList", permissionList);
        model.addAttribute("menuIdList", menuIdList);
        model.addAttribute("permissionIdList", permissionIdList);
        System.out.println("menuIdList:"+menuIdList);
        System.out.println("permissionIdList:"+permissionIdList);
        return "backstage/html/menu/role-modify";
    }
    @PostMapping("/role/modify")
    @ResponseBody
    @RequiresPermissions(value = {"root", "role:manage"}, logical = Logical.OR)
    public ResultVO roleModify_post(int id, @RequestParam List<Integer> menu_ids, @RequestParam List<Integer> permission_ids) {
        ResultVO vo = new ResultVO();
        int modifyRole = roleService.modifyRole(id, menu_ids, permission_ids);
        if (modifyRole == 1) {
            vo.setCode(200);
            vo.setMsg("修改成功");
        } else {
            vo.setCode(500);
            vo.setMsg("修改失败");
        }
        return vo;
    }
    //role add
    @GetMapping("/role/add")
    @RequiresPermissions(value = {"root", "role:manage"}, logical = Logical.OR)
    public String roleAdd(Model model) {
        //获取所有菜单
        List<SysMenu> menuList = menuService.getAllMenuList();
        //获取所有权限
        List<SysPermission> permissionList = permissionService.getAllPermission();
        model.addAttribute("menuList", menuList);
        model.addAttribute("permissionList", permissionList);
        return "backstage/html/menu/role-add";
    }
    @PostMapping("/role/add")
    @ResponseBody
    @RequiresPermissions(value = {"root", "role:manage"}, logical = Logical.OR)
    @Transactional(isolation = Isolation.DEFAULT)
    public ResultVO roleAdd_post(String description, String role, @RequestParam List<Integer> menu_ids, @RequestParam List<Integer> permission_ids) {
        SysRole sysRole = new SysRole();
        sysRole.setDescription(description);
        sysRole.setRole(role);
        ResultVO vo = new ResultVO();
        int addRole = roleService.addRole(sysRole);
        if (addRole == 1) {
            int roleId = roleService.getMaxRoleId();
            int bindMenus = 0;
            int bindPermissions = 0;
            if (menu_ids.size() != 0) {
                bindMenus = roleService.bindMenus(roleId, menu_ids);
            }
            if (permission_ids.size() != 0) {
                bindPermissions = roleService.bindPermissions(roleId, permission_ids);
            }
            if (bindMenus == menu_ids.size() && bindPermissions == permission_ids.size()) {
                vo.setCode(200);
                vo.setMsg("添加成功");
            } else {
                vo.setCode(500);
                vo.setMsg("添加失败");
            }
        } else {
            vo.setCode(500);
            vo.setMsg("添加失败");
        }
        return vo;
    }
    //role delete
    @PostMapping("/role/delete")
    @ResponseBody
    @RequiresPermissions(value = {"root", "role:manage"}, logical = Logical.OR)
    public ResultVO roleDelete(@RequestParam List<Integer> ids) {
        ResultVO resultVO = new ResultVO();
        int i = roleService.deleteRoles(ids);
        if (i == 1) {
            resultVO.setCode(200);
            resultVO.setMsg("删除成功！");
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("删除失败！");
        }
        return resultVO;
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
        List<Animal> animalList = petManageService.findAll(PetStatus.ALL.getCode(),"id", "desc");
        model.addAttribute("animalList", animalList);
        return "backstage/html/menu/pet-list2";
    }
    //查看宠物信息
    @GetMapping("/pet/list")
    @RequiresPermissions("pet:all")
    public String petInfo(Model model) throws ParseException {
        //查询所有宠物信息
        List<Animal> animalList = petManageService.findAll(PetStatus.ALL.getCode(), "id", "desc");
        model.addAttribute("animalList", animalList);
        return "backstage/html/menu/pet-list2";
    }
    //查看单个宠物信息
    @GetMapping("/pet/info/{id}")
    @RequiresPermissions("pet:all")
    public String petInfo(@PathVariable("id") Integer id, Model model) {
        Animal animal = petManageService.findPetById(id);
        model.addAttribute("animal", animal);
        return "backstage/html/menu/pet-detail";
    }

    @GetMapping("/pet/list_query")
    @ResponseBody
    @RequiresPermissions("pet:all")
    public Map<String, Object> petInfo_query(RequestParamsPetList params) throws ParseException {
        System.out.println(params);
        int status = params.getSearch_status();
        PageHelper.startPage(params.getPageNumber(),params.getPageSize());
        List<Animal> animalList = petManageService.findAll(status, params.getSort(), params.getOrder());
        List<Animal> animalList_size = petManageService.findAll(PetStatus.ALL.getCode(), params.getSort(), params.getOrder());
        int total = animalList_size.size();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", total);
        hashMap.put("rows", animalList);
        return hashMap;

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
    //上传宠物图片
    @PostMapping("/pet/upload/{id}")
    @ResponseBody
    @RequiresPermissions("pet:all")
    public ResultVO petUpload( MultipartFile file, @PathVariable Integer id) throws IOException {
        ResultVO resultVO = new ResultVO();
        if (file.isEmpty()) {
            resultVO.setCode(500);
            resultVO.setMsg("请选择图片");
            return resultVO;
        }
        // 原始文件名称
        String originalFilename = file.getOriginalFilename();
        // 唯一的文件名称
        String fileName = id + "/" + UUID.randomUUID() + "." + originalFilename;
        // 上传地址
        OSSUtil ossUtil = new OSSUtil();
        String url = ossUtil.uploadImg2Oss(file, fileName);
        if (url != null) {
            if (id>0) {
                int i = petManageService.insertPetImg(id, url);
                if (i > 0) {
                    resultVO.setCode(200);
                    resultVO.setMsg("上传成功");
                    resultVO.setResult(url);
                }else {
                    resultVO.setCode(500);
                    resultVO.setMsg("上传失败");
                }
            } else {
                resultVO.setCode(200);
                resultVO.setMsg("上传成功");
                resultVO.setResult(url);
            }
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("上传失败");
        }
        return resultVO;
    }
    //查找宠物图片
    @GetMapping("/pet/img/{id}")
    @ResponseBody
    @RequiresPermissions("pet:all")
    public ResultVO petImg(@PathVariable Integer id) {
        String pic = petManageService.findPetPicById(id);
        ResultVO resultVO = new ResultVO();
        if (pic != null) {
            resultVO.setCode(200);
            resultVO.setMsg("查找成功！");
            resultVO.setResult(pic);
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("查找失败！");
        }
        return resultVO;
    }
    //修改宠物信息
    @GetMapping("/pet/edit/{id}")
    @RequiresPermissions("pet:all")
    public String petEdit(@PathVariable("id") Integer id, Model model) {
        Animal animal = petManageService.findPetById(id);
        model.addAttribute("animal", animal);
        return "backstage/html/menu/pet-edit";
    }
    @PostMapping("/pet/edit")
    @RequiresPermissions("pet:all")
    public String petEdit(Animal animal, Model model ) {
        int re = petManageService.updatePet(animal);
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
    public ResultVO petDelete(int[] ids) {
        ResultVO resultVO = new ResultVO();
        int i = petManageService.deletePets(ids);
        if (i == ids.length) {
            resultVO.setCode(200);
            resultVO.setMsg("删除成功！");
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("删除失败！");
        }
        return resultVO;
    }

    //feedback管理
    @GetMapping("/feedback")
    @RequiresPermissions("feedback:all")
    public String feedback() {
        return "backstage/html/menu/feedback";
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
    @GetMapping("/approval/pet_list")
    @ResponseBody
    @RequiresPermissions("approval:all")
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
    @RequiresPermissions("approval:all")
    public String approvalWeb(@PathVariable("petId") Integer petId, @PathVariable("userId") Integer userId, Model model) {
        Animal animal = petManageService.findPetById(petId);
        User user = personalService.queryUserInfoById(userId);
        model.addAttribute("animal", animal);
        model.addAttribute("user", user);
        return "backstage/html/menu/approval-pet-web";
    }
    @PostMapping("/approval/edit")
    @ResponseBody
    @RequiresPermissions("approval:all")
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
            resultVO.setMsg("修改成功！");
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("修改失败！");
        }
        return resultVO;
    }

    //查看用户信息
    @GetMapping("/user/info/{id}")
    @RequiresPermissions("approval:all")
    public String userInfo(@PathVariable("id") Integer id, Model model) {
        User user = personalService.queryUserInfoById(id);
        model.addAttribute("user", user);
        return "user-detail";
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
    public String news(Model model) {
//        List<Article> articleList = articleService.queryArticles(ArticleType.NEWS.getCode(), ArticleStatus.ALL.getCode(), "id", "asc");
//        model.addAttribute("articleList", articleList);
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
