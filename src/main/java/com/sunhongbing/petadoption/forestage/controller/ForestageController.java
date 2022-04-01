package com.sunhongbing.petadoption.forestage.controller;

import com.sunhongbing.petadoption.backstage.entity.Animal;
import com.sunhongbing.petadoption.backstage.entity.Article;
import com.sunhongbing.petadoption.backstage.entity.Banner;
import com.sunhongbing.petadoption.backstage.entity.User;
import com.sunhongbing.petadoption.backstage.result.ResultVO;
import com.sunhongbing.petadoption.backstage.service.ArticleService;
import com.sunhongbing.petadoption.backstage.service.PetManageService;
import com.sunhongbing.petadoption.forestage.entity.ApplyAnimal;
import com.sunhongbing.petadoption.forestage.entity.EditParam;
import com.sunhongbing.petadoption.forestage.entity.EditPasswordParam;
import com.sunhongbing.petadoption.forestage.service.PersonalService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 孙红兵
 * @className ForestageController
 * @description TODO
 * @studentId 202018113175
 * @date 2021/12/12 21:20
 */
@Controller
@RequestMapping("/fore")
public class ForestageController {

    @Autowired
    private PersonalService personalService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private PetManageService petManageService;


    //logback
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 访问首页
    @GetMapping("/")
    public String index2(Model model) {
        // 随机查询5只宠物
        List<Animal> randomPets = petManageService.getRandomPets(5);
        Animal bannerPet = randomPets.get(0);
        // 查询Banner
        List<Banner> bannerList = articleService.queryBanners("id", "asc");
        model.addAttribute("bannerList", bannerList);
        model.addAttribute("bannerPet", bannerPet);
        model.addAttribute("randomPets", randomPets);
        return "forestage/index";
    }

    //fore banner detail
    @GetMapping("/banner/{id}")
    public String banner(@PathVariable("id") int id, Model model){
        Banner banner = articleService.getBannerById(id);
        model.addAttribute("banner",banner);
        return "forestage/keep-in-touch/banner-detail";
    }

    //home
    @GetMapping("/home")
    public String home(Model model) {
        try {
            int userId = (int) SecurityUtils.getSubject().getPrincipal();
            User user = personalService.queryUserInfoById(userId);
            model.addAttribute("user", user);
        } catch (Exception e) {
            return "redirect:/login";
        }
        return "forestage/home";
    }

    //persom_info
    @GetMapping("/personal_info")
    @RequiresPermissions("user:all")
    public String personal_info(Model model) {
        try {
            int userId = (int) SecurityUtils.getSubject().getPrincipal();
            User user = personalService.queryUserInfoById(userId);
            model.addAttribute("user", user);
        } catch (Exception e) {
            return "redirect:/login";
        }
        return "forestage/person-info";
    }

    //personal_info_edit
    @GetMapping("/personal_info_edit")
    @RequiresPermissions("user:all")
    public String personal_info_edit(Model model) {
        try {
            int userId = (int) SecurityUtils.getSubject().getPrincipal();
            User user = personalService.queryUserInfoById(userId);
            model.addAttribute("user", user);
        } catch (Exception e) {
            return "redirect:/login";
        }
        return "forestage/personal-info-edit";
    }
    //personal_info_edit post
    @PostMapping("/personal_info_edit")
    @RequiresPermissions("user:all")
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
    @RequiresPermissions("user:all")
    public String apply_list(Model model) {
        try {
            int userId = (int) SecurityUtils.getSubject().getPrincipal();
            List<ApplyAnimal> applyAnimalList = personalService.queryAdoptList(userId);
            model.addAttribute("animalList", applyAnimalList);
        } catch (Exception e) {
            return "redirect:/login";
        }
        return "forestage/apply-list";
    }

    //editPwd
    @GetMapping("/editPwd")
    @RequiresPermissions("user:all")
    public String editPwd(Model model) {
        try {
            int userId = (int) SecurityUtils.getSubject().getPrincipal();
        } catch (Exception e) {
            return "redirect:/login";
        }
        return "forestage/edit-password";
    }
    //editPwd post
    @PostMapping("/editPwd")
    @RequiresPermissions("user:all")
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




    //fore article detail
    @GetMapping("/article/{id}")
    public String articleDetail(@PathVariable Integer id, Model model) {
        Article article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "forestage/article";
    }

}
