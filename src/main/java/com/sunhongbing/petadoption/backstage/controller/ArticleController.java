package com.sunhongbing.petadoption.backstage.controller;

import com.github.pagehelper.PageHelper;
import com.sunhongbing.petadoption.backstage.entity.Article;
import com.sunhongbing.petadoption.backstage.entity.Banner;
import com.sunhongbing.petadoption.backstage.entity.RequestParamsPetList;
import com.sunhongbing.petadoption.backstage.enums.ArticleStatus;
import com.sunhongbing.petadoption.backstage.enums.ArticleType;
import com.sunhongbing.petadoption.backstage.result.ResultVO;
import com.sunhongbing.petadoption.backstage.service.ArticleService;
import com.sunhongbing.petadoption.config.OSSUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @className: ArticleController
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-03-27 17:36
 */
@Controller
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("/{id}")
    @RequiresPermissions("article:all")
    public String show(@PathVariable("id") int id, Model model){
        Article article = articleService.getArticleById(id);
        model.addAttribute("article",article);
        return "backstage/html/menu/article";
    }

    //banner detail
    @GetMapping("/banner/{id}")
    @RequiresPermissions("article:all")
    public String banner(@PathVariable("id") int id, Model model){
        Banner banner = articleService.getBannerById(id);
        model.addAttribute("banner",banner);
        return "backstage/html/menu/banner-detail";
    }

    @GetMapping("/feedback/detail/{id}")
    @RequiresPermissions("article:all")
    public String feedback_detail(@PathVariable("id") int id, Model model){
        Article article = articleService.getArticleById(id);
        model.addAttribute("article",article);
        return "backstage/html/menu/feedback_detail";
    }

    //??????????????????
    @GetMapping("/toEditor")
    @RequiresPermissions("article:all")
    public String toEditor(){
        return "backstage/html/menu/editor";
    }

    // banner ????????????
    @GetMapping("/banner/toEditor")
    @RequiresPermissions("article:all")
    public String toBannerEditor(){
        return "backstage/html/menu/banner_editor";
    }

    //?????? banner ??????
    @PostMapping("/banner/upload")
    @ResponseBody
    @RequiresPermissions("article:all")
    public ResultVO petUpload(@RequestParam("cover_file") MultipartFile file) throws IOException {
        ResultVO resultVO = new ResultVO();
        if (file.isEmpty()) {
            resultVO.setCode(500);
            resultVO.setMsg("???????????????");
            return resultVO;
        }
        // ??????????????????
        String originalFilename = file.getOriginalFilename();
        // ?????????????????????
        String fileName = "Cover/" + UUID.randomUUID() + "." + originalFilename;
        // ????????????
        OSSUtil ossUtil = new OSSUtil();
        String url = ossUtil.uploadImg2Oss(file, fileName);
        if (url != null) {
            resultVO.setCode(200);
            resultVO.setMsg("????????????");
            resultVO.setResult(url);
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("????????????");
        }
        return resultVO;
    }

    // ????????????
    @PostMapping("/addArticle")
    @RequiresPermissions("article:all")
    public String addArticle(Article article, Model model){
        String emptyUrl = "backstage/html/menu/editor";
        if (article.getTitle() == null || article.getTitle().equals("")){
            model.addAttribute("msg","??????????????????");
            return emptyUrl;
        }
        if (article.getAuthor() == null || article.getAuthor().equals("")){
            model.addAttribute("msg","??????????????????");
            return emptyUrl;
        }
        if (article.getContent() == null || article.getContent().equals("")){
            model.addAttribute("msg","??????????????????");
            return emptyUrl;
        }

        int i = articleService.addArticle(article);
        if(i == 1){
            Date ss = new Date();
            SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format0.format(ss.getTime());
            article.setCreateTime(time);
            model.addAttribute("article",article);
            model.addAttribute("msg","?????????,???????????????");
        }
        return "backstage/html/menu/article";
    }

    // add Banner
    @PostMapping("/addBanner")
    @RequiresPermissions("article:all")
    public String addBanner(Banner banner, @RequestParam("cover_url") String cover, Model model){
        String emptyUrl = "backstage/html/menu/banner_editor";
        if (banner.getTitle() == null || banner.getTitle().equals("")){
            model.addAttribute("msg","??????????????????");
            return emptyUrl;
        }
        if (banner.getAuthor() == null || banner.getAuthor().equals("")){
            model.addAttribute("msg","??????????????????");
            return emptyUrl;
        }
        if (cover == null || cover.equals("")){
            model.addAttribute("msg","??????????????????");
            return emptyUrl;
        }
        if (banner.getContent() == null || banner.getContent().equals("")){
            model.addAttribute("msg","??????????????????");
            return emptyUrl;
        }

        banner.setCover(cover);
        int i = articleService.addBanner(banner);
        if(i == 1){
            Date ss = new Date();
            SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format0.format(ss.getTime());
            banner.setCreateTime(time);
            model.addAttribute("msg","????????????");
        }
        return "backstage/html/menu/banner";
    }

    //???????????????OSS
    @PostMapping("/file/upload")
    @RequiresPermissions("article:all")
    @ResponseBody
    public String fileUpload(@RequestParam(value = "editormd-image-file") MultipartFile file) throws IOException, JSONException {
        Calendar instance = Calendar.getInstance();
        String year = String.valueOf(instance.get(Calendar.YEAR));
        String month = (instance.get(Calendar.MONTH) + 1)+"???";

        // ??????????????????
        String originalFilename = file.getOriginalFilename();
        // ?????????????????????
        String fileName = "article" + "/" + year + "/" + month + "/"
                +  UUID.randomUUID().toString().replaceAll("-", "")
                + "." + originalFilename;

        // ????????????
        OSSUtil ossUtil = new OSSUtil();
        String url = ossUtil.uploadImg2Oss(file, fileName);

        //???editormd????????????
        JSONObject res = new JSONObject();
        res.put("url", url);
        res.put("success", 1);
        res.put("message", "upload success!");

        return res.toString();
    }

    //????????????
    @GetMapping("/news/list_query")
    @RequiresPermissions("article:all")
    @ResponseBody
    public Map<String, Object> newsList_query(RequestParamsPetList params) {
        int status = params.getSearch_status();
        List<Article> articleList;
        List<Article> articleList_size;
        PageHelper.startPage(params.getPageNumber(),params.getPageSize());
        articleList = articleService.queryArticles(ArticleType.NEWS.getCode(), status, params.getSort(), params.getOrder());
        articleList_size = articleService.queryArticles(ArticleType.NEWS.getCode(), status, params.getSort(), params.getOrder());
        int total = articleList_size.size();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", total);
        hashMap.put("rows", articleList);
        return hashMap;
    }
    //????????????
    @GetMapping("/activity/list_query")
    @RequiresPermissions("article:all")
    @ResponseBody
    public Map<String, Object> activityList_query(RequestParamsPetList params) {
        int status = params.getSearch_status();
        List<Article> articleList;
        List<Article> articleList_size;
        PageHelper.startPage(params.getPageNumber(),params.getPageSize());
        articleList = articleService.queryArticles(ArticleType.ACTIVITY.getCode(), status, params.getSort(), params.getOrder());
        articleList_size = articleService.queryArticles(ArticleType.ACTIVITY.getCode(), status, params.getSort(), params.getOrder());
        int total = articleList_size.size();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", total);
        hashMap.put("rows", articleList);
        return hashMap;
    }
    //????????????
    @GetMapping("/article/list_query")
    @RequiresPermissions("article:all")
    @ResponseBody
    public Map<String, Object> articleList_query(RequestParamsPetList params) {
        int status = params.getSearch_status();
        List<Article> articleList;
        List<Article> articleList_size;
        PageHelper.startPage(params.getPageNumber(),params.getPageSize());
        articleList = articleService.queryArticles(ArticleType.ARTICLE.getCode(), status, params.getSort(), params.getOrder());
        articleList_size = articleService.queryArticles(ArticleType.ARTICLE.getCode(), status, params.getSort(), params.getOrder());
        int total = articleList_size.size();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", total);
        hashMap.put("rows", articleList);
        return hashMap;
    }
    //????????????
    @GetMapping("/happy-adoption/list_query")
    @RequiresPermissions("article:all")
    @ResponseBody
    public Map<String, Object> happy_adoption_query(RequestParamsPetList params) {
        int status = params.getSearch_status();
        List<Article> articleList;
        List<Article> articleList_size;
        PageHelper.startPage(params.getPageNumber(),params.getPageSize());
        articleList = articleService.queryArticles(ArticleType.HAPPY.getCode(), status, params.getSort(), params.getOrder());
        articleList_size = articleService.queryArticles(ArticleType.HAPPY.getCode(), status, params.getSort(), params.getOrder());
        int total = articleList_size.size();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", total);
        hashMap.put("rows", articleList);
        return hashMap;
    }
    //banner
    @GetMapping("/banner/list_query")
    @RequiresPermissions("article:all")
    @ResponseBody
    public Map<String, Object> bannerList_query(RequestParamsPetList params) {
        List<Banner> articleList;
        List<Banner> articleList_size;
        PageHelper.startPage(params.getPageNumber(),params.getPageSize());
        articleList = articleService.queryBanners(params.getSort(), params.getOrder());
        articleList_size = articleService.queryBanners(params.getSort(), params.getOrder());
        int total = articleList_size.size();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", total);
        hashMap.put("rows", articleList);
        return hashMap;
    }
    //about-us
    @GetMapping("/about-us/list_query")
    @RequiresPermissions("article:all")
    @ResponseBody
    public Map<String, Object> about_usList_query(RequestParamsPetList params) {
        int status = params.getSearch_status();
        List<Article> articleList;
        List<Article> articleList_size;
        int[] types = new int[]{
                ArticleType.ADOPTION_METHOD.getCode(), ArticleType.ABOUT_ORGANIZATION.getCode(),
                ArticleType.ABOUT_TIME_PLACE.getCode(), ArticleType.ABOUT_QUESTION.getCode()
        };
        PageHelper.startPage(params.getPageNumber(),params.getPageSize());
        articleList = articleService.queryArticlesByTypes(types, status, params.getSort(), params.getOrder());
        articleList_size = articleService.queryArticlesByTypes(types, status, params.getSort(), params.getOrder());
        int total = articleList_size.size();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", total);
        hashMap.put("rows", articleList);
        return hashMap;
    }


    // feedback ??????
    @GetMapping("/feedback/list_query")
    @RequiresPermissions("feedback:all")
    @ResponseBody
    public Map<String, Object> feedbackList_query(RequestParamsPetList params) {
        int status = params.getSearch_status();
        List<Article> articleList;
        List<Article> articleList_size;
        int[] types = new int[]{
                ArticleType.FEEDBACK_GENERAL.getCode(), ArticleType.FEEDBACK_ADOPTION.getCode(),
                ArticleType.FEEDBACK_COMPLAINT.getCode(), ArticleType.FEEDBACK_OTHER.getCode()
        };
        PageHelper.startPage(params.getPageNumber(),params.getPageSize());
        articleList = articleService.queryArticlesByTypes(types, status, params.getSort(), params.getOrder());
        articleList_size = articleService.queryArticlesByTypes(types, status, params.getSort(), params.getOrder());
        int total = articleList_size.size();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", total);
        hashMap.put("rows", articleList);
        return hashMap;
    }
    // ??????????????????
    @GetMapping("/all/list_query")
    @RequiresPermissions("approval:pet")
    @ResponseBody
    public Map<String, Object> allList_query(RequestParamsPetList params) {
        int status = params.getSearch_status();
        List<Article> articleList;
        List<Article> articleList_size;
        int[] types = new int[]{
                ArticleType.NEWS.getCode(), ArticleType.ACTIVITY.getCode(),
                ArticleType.NOTICE.getCode(), ArticleType.ARTICLE.getCode(), ArticleType.HAPPY.getCode(),
                ArticleType.ADOPTION_METHOD.getCode(), ArticleType.ABOUT_ORGANIZATION.getCode(),
                ArticleType.ABOUT_TIME_PLACE.getCode(), ArticleType.ABOUT_QUESTION.getCode()
        };
        PageHelper.startPage(params.getPageNumber(),params.getPageSize());
        articleList = articleService.queryArticlesByTypes(types, status, params.getSort(), params.getOrder());
        articleList_size = articleService.queryArticlesByTypes(types, status, params.getSort(), params.getOrder());
        int total = articleList_size.size();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", total);
        hashMap.put("rows", articleList);
        return hashMap;
    }


    //????????????
    @PostMapping("/delete")
    @RequiresPermissions("article:delete")
    @ResponseBody
    public ResultVO deleteArticles(int[] ids) {
        ResultVO resultVO = new ResultVO();
        int i = articleService.deleteArticleByIds(ids);
        if (i == ids.length) {
            resultVO.setCode(200);
            resultVO.setMsg("???????????????");
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("???????????????");
        }
        return resultVO;
    }

    //??????banner
    @PostMapping("/banner/delete")
    @RequiresPermissions("article:delete")
    @ResponseBody
    public ResultVO deleteBanner(int[] ids) {
        ResultVO resultVO = new ResultVO();
        int i = articleService.deleteBannerByIds(ids);
        if (i == ids.length) {
            resultVO.setCode(200);
            resultVO.setMsg("???????????????");
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("???????????????");
        }
        return resultVO;
    }

    //????????????
    @GetMapping("/approve/{id}")
    @RequiresPermissions("approval:article")
    public String approvalArticle(Model model, @PathVariable int id) {
        Article article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "backstage/html/menu/approval-article-detail";
    }
    @PostMapping("/approve_post")
    @ResponseBody
    @RequiresPermissions("approval:article")
    public ResultVO approvalArticle_post(int id,int status) {
        ResultVO resultVO = new ResultVO();
        if (status == ArticleStatus.PASS.getCode()) {
            int i = articleService.pass(id);
            if (i == 1) {
                resultVO.setCode(200);
                resultVO.setMsg("OK???");
            } else {
                resultVO.setCode(500);
                resultVO.setMsg("??????????????????");
            }
        } else if (status == ArticleStatus.REJECT.getCode()) {
            int i = articleService.reject(id);
            if (i == 1) {
                resultVO.setCode(200);
                resultVO.setMsg("OK???");
            } else {
                resultVO.setCode(500);
                resultVO.setMsg("??????????????????");
            }
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("Error???");
        }
        return resultVO;
    }




}