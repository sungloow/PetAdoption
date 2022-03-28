package com.sunhongbing.petadoption.forestage.controller;

import com.sunhongbing.petadoption.backstage.entity.Article;
import com.sunhongbing.petadoption.backstage.entity.User;
import com.sunhongbing.petadoption.backstage.enums.ArticleType;
import com.sunhongbing.petadoption.backstage.result.ResultVO;
import com.sunhongbing.petadoption.backstage.service.ArticleService;
import com.sunhongbing.petadoption.backstage.service.UserService;
import com.sunhongbing.petadoption.forestage.Utils;
import com.sunhongbing.petadoption.forestage.entity.AdoptionStatus;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @className: AboutUsController
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-10 17:43
 */
@Controller
@RequestMapping("/about-us")
public class AboutUsController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserService userService;

    // introduction.html
    @RequestMapping("/introduction")
    public String index(Model model) {
        List<Article> articleList = articleService.queryArticles(ArticleType.ABOUT_ORGANIZATION.getCode(), AdoptionStatus.ACCEPT.getCode(), "id", "asc");
        Article article = Utils.handlerAboutUsArticle(articleList);
        model.addAttribute("article", article);
        return "forestage/about-us/introduction";
    }

    // contact.html
    @GetMapping("/contact")
    public String contact() {
        return "forestage/about-us/contact";
    }
    //contact post
    @PostMapping("/contact")
    @ResponseBody
    public ResultVO contactPost(Article article) {
        ResultVO vo = new ResultVO();
        int userId;
        try {
            userId = (int) SecurityUtils.getSubject().getPrincipal();
        } catch (Exception e) {
            vo.setCode(500);
            vo.setMsg("请先登录");
            return vo;
        }
        User user = userService.getUserById(userId);
        article.setAuthor(user.getEmail());
        int i = articleService.addArticle(article);
        if (i == 1) {
            vo.setCode(200);
            vo.setMsg("提交成功");
        } else {
            vo.setCode(500);
            vo.setMsg("提交失败");
        }
        return vo;
    }

    // working-time.html
    @RequestMapping("/working-time")
    public String workingTime(Model model) {
        List<Article> articleList = articleService.queryArticles(ArticleType.ABOUT_TIME_PLACE.getCode(), AdoptionStatus.ACCEPT.getCode(), "id", "asc");
        Article article = Utils.handlerAboutUsArticle(articleList);
        model.addAttribute("article", article);
        return "forestage/about-us/working-time";
    }

    // faq.html
    @RequestMapping("/faq")
    public String faq(Model model) {
        List<Article> articleList = articleService.queryArticles(ArticleType.ABOUT_QUESTION.getCode(), AdoptionStatus.ACCEPT.getCode(), "id", "asc");
        Article article = Utils.handlerAboutUsArticle(articleList);
        model.addAttribute("article", article);
        return "forestage/about-us/faq";
    }
}
