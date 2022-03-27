package com.sunhongbing.petadoption.backstage.controller;

import com.github.pagehelper.PageHelper;
import com.sunhongbing.petadoption.backstage.dao.ArticleMapper;
import com.sunhongbing.petadoption.backstage.entity.Animal;
import com.sunhongbing.petadoption.backstage.entity.Article;
import com.sunhongbing.petadoption.backstage.entity.RequestParamsPetList;
import com.sunhongbing.petadoption.backstage.enums.ArticleStatus;
import com.sunhongbing.petadoption.backstage.enums.ArticleType;
import com.sunhongbing.petadoption.backstage.enums.PetStatus;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
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

    @GetMapping("/toEditor")
    @RequiresPermissions("article:all")
    public String toEditor(){
        return "backstage/html/menu/editor";
    }

    @PostMapping("/addArticle")
    @RequiresPermissions("article:all")
    public String addArticle(Article article, Model model){
        int i = articleService.addArticle(article);
        if(i == 1){
            Date ss = new Date();
            SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format0.format(ss.getTime());
            article.setCreateTime(time);
            model.addAttribute("article",article);
            model.addAttribute("msg","已发布,请等待审核");
        }
        return "backstage/html/menu/article";
    }

    //上传图片
    @PostMapping("/file/upload")
    @RequiresPermissions("article:all")
    @ResponseBody
    public String fileUpload(@RequestParam(value = "editormd-image-file") MultipartFile file) throws IOException, JSONException {
        Calendar instance = Calendar.getInstance();
        String year = String.valueOf(instance.get(Calendar.YEAR));
        String month = (instance.get(Calendar.MONTH) + 1)+"月";

        // 原始文件名称
        String originalFilename = file.getOriginalFilename();
        // 唯一的文件名称
        String fileName = "article" + "/" + year + "/" + month + "/"
                +  UUID.randomUUID().toString().replaceAll("-", "")
                + "." + originalFilename;

        // 上传地址
        OSSUtil ossUtil = new OSSUtil();
        String url = ossUtil.uploadImg2Oss(file, fileName);

        //给editormd进行回调
        JSONObject res = new JSONObject();
        res.put("url", url);
        res.put("success", 1);
        res.put("message", "upload success!");

        return res.toString();
    }

    //新闻列表
    @GetMapping("/news/list_query")
    @RequiresPermissions("article:all")
    @ResponseBody
    public Map<String, Object> newsList_query(RequestParamsPetList params) {
        System.out.println(params);
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
    //活动列表
    @GetMapping("/activity/list_query")
    @RequiresPermissions("article:all")
    @ResponseBody
    public Map<String, Object> activityList_query(RequestParamsPetList params) {
        System.out.println(params);
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
    //文章列表
    @GetMapping("/article/list_query")
    @RequiresPermissions("article:all")
    @ResponseBody
    public Map<String, Object> articleList_query(RequestParamsPetList params) {
        System.out.println(params);
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
    //all
    @GetMapping("/all/list_query")
    @RequiresPermissions("approval:all")
    @ResponseBody
    public Map<String, Object> allList_query(RequestParamsPetList params) {
        System.out.println(params);
        int status = params.getSearch_status();
        List<Article> articleList;
        List<Article> articleList_size;
        PageHelper.startPage(params.getPageNumber(),params.getPageSize());
        articleList = articleService.queryArticles(status, params.getSort(), params.getOrder());
        articleList_size = articleService.queryArticles(status, params.getSort(), params.getOrder());
        int total = articleList_size.size();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", total);
        hashMap.put("rows", articleList);
        return hashMap;
    }


    //删除文章
    @PostMapping("/delete")
    @RequiresPermissions("article:all")
    @ResponseBody
    public ResultVO deleteArticles(int[] ids) {
        ResultVO resultVO = new ResultVO();
        int i = articleService.deleteArticleByIds(ids);
        if (i == ids.length) {
            resultVO.setCode(200);
            resultVO.setMsg("删除成功！");
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("删除失败！");
        }
        return resultVO;
    }

    //审核文章
    @GetMapping("/approve/{id}")
    @RequiresPermissions("approval:all")
    public String approvalArticle(Model model, @PathVariable int id) {
        Article article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "backstage/html/menu/approval-article-detail";
    }
    @PostMapping("/approve_post")
    @ResponseBody
    @RequiresPermissions("approval:all")
    public ResultVO approvalArticle_post(int id,int status) {
        ResultVO resultVO = new ResultVO();
        System.out.println("id:"+id+"  status:"+status);
        if (status == ArticleStatus.PASS.getCode()) {
            int i = articleService.pass(id);
            if (i == 1) {
                resultVO.setCode(200);
                resultVO.setMsg("审核成功！");
            } else {
                resultVO.setCode(500);
                resultVO.setMsg("审核失败！");
            }
        } else if (status == ArticleStatus.REJECT.getCode()) {
            int i = articleService.reject(id);
            if (i == 1) {
                resultVO.setCode(200);
                resultVO.setMsg("审核成功！");
            } else {
                resultVO.setCode(500);
                resultVO.setMsg("审核失败！");
            }
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("Error！");
        }
        return resultVO;
    }



}