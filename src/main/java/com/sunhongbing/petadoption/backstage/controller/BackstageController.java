package com.sunhongbing.petadoption.backstage.controller;

import com.github.pagehelper.PageHelper;
import com.sunhongbing.petadoption.backstage.dao.ArticleMapper;
import com.sunhongbing.petadoption.backstage.entity.*;
import com.sunhongbing.petadoption.backstage.enums.ApplyStatus;
import com.sunhongbing.petadoption.backstage.enums.ArticleStatus;
import com.sunhongbing.petadoption.backstage.enums.ArticleType;
import com.sunhongbing.petadoption.backstage.enums.PetStatus;
import com.sunhongbing.petadoption.backstage.service.ArticleService;
import com.sunhongbing.petadoption.backstage.service.PetManageService;
import com.sunhongbing.petadoption.backstage.result.ResultVO;
import com.sunhongbing.petadoption.backstage.service.MenuService;
import com.sunhongbing.petadoption.config.OSSUtil;
import com.sunhongbing.petadoption.forestage.entity.ApplyRecord;
import com.sunhongbing.petadoption.forestage.service.AdoptionService;
import com.sunhongbing.petadoption.forestage.service.PersonalService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
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
        return "backstage/html/menu/user-info";
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
