package com.sunhongbing.petadoption.forestage.controller;

import com.sunhongbing.petadoption.backstage.entity.Animal;
import com.sunhongbing.petadoption.backstage.entity.Article;
import com.sunhongbing.petadoption.backstage.entity.Banner;
import com.sunhongbing.petadoption.backstage.service.ArticleService;
import com.sunhongbing.petadoption.backstage.service.PetManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private ArticleService articleService;
    @Autowired
    private PetManageService petManageService;


    //logback
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 访问首页
    @GetMapping("/")
    public String index(Model model) {
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

    //fore article detail
    @GetMapping("/article/{id}")
    public String articleDetail(@PathVariable Integer id, Model model) {
        Article article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "forestage/article";
    }

}
