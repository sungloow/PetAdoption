package com.sunhongbing.petadoption.forestage.controller;

import com.sunhongbing.petadoption.backstage.entity.Article;
import com.sunhongbing.petadoption.backstage.enums.ArticleStatus;
import com.sunhongbing.petadoption.backstage.enums.ArticleType;
import com.sunhongbing.petadoption.backstage.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @className: KeepInTouchController
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-10 17:41
 */
@Controller
@RequestMapping("/keep-in-touch")
public class KeepInTouchController {
    @Autowired
    private ArticleService articleService;

    // news.html
    @GetMapping("/news")
    public String index(Model model) {
        List<Article> articleList = articleService.queryArticles(ArticleType.NEWS.getCode(), ArticleStatus.PASS.getCode(), "id", "asc");
        model.addAttribute("articleList", articleList);
        return "forestage/keep-in-touch/news";
    }

    // activity.html
    @GetMapping("/activity")
    public String activity(Model model) {
        List<Article> articleList = articleService.queryArticles(ArticleType.ACTIVITY.getCode(), ArticleStatus.PASS.getCode(), "id", "asc");
        model.addAttribute("articleList", articleList);
        return "forestage/keep-in-touch/activity";
    }

}
