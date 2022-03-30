package com.sunhongbing.petadoption.backstage.controller;

import com.github.pagehelper.PageHelper;
import com.sunhongbing.petadoption.backstage.dao.ArticleMapper;
import com.sunhongbing.petadoption.backstage.entity.Animal;
import com.sunhongbing.petadoption.backstage.entity.Article;
import com.sunhongbing.petadoption.backstage.entity.Banner;
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
import javax.websocket.server.PathParam;
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

    //文章编辑页面
    @GetMapping("/toEditor")
    @RequiresPermissions("article:all")
    public String toEditor(){
        return "backstage/html/menu/editor";
    }

    // banner 编辑页面
    @GetMapping("/banner/toEditor")
    @RequiresPermissions("article:all")
    public String toBannerEditor(){
        return "backstage/html/menu/banner_editor";
    }

    //上传 banner 图片
    @PostMapping("/banner/upload")
    @ResponseBody
    @RequiresPermissions("article:all")
    public ResultVO petUpload(@RequestParam("cover_file") MultipartFile file) throws IOException {
        ResultVO resultVO = new ResultVO();
        if (file.isEmpty()) {
            resultVO.setCode(500);
            resultVO.setMsg("请选择图片");
            return resultVO;
        }
        // 原始文件名称
        String originalFilename = file.getOriginalFilename();
        // 唯一的文件名称
        String fileName = "Cover/" + UUID.randomUUID() + "." + originalFilename;
        // 上传地址
        OSSUtil ossUtil = new OSSUtil();
        String url = ossUtil.uploadImg2Oss(file, fileName);
        if (url != null) {
            resultVO.setCode(200);
            resultVO.setMsg("上传成功");
            resultVO.setResult(url);
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("上传失败");
        }
        return resultVO;
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

    // add Banner
    @PostMapping("/addBanner")
    @RequiresPermissions("article:all")
    public String addBanner(Banner banner, @RequestParam("cover_url") String cover, Model model){
        banner.setCover(cover);
        int i = articleService.addBanner(banner);
        if(i == 1){
            Date ss = new Date();
            SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format0.format(ss.getTime());
            banner.setCreateTime(time);
//            model.addAttribute("banner",banner);
            model.addAttribute("msg","发布成功");
        }
        return "backstage/html/menu/banner";
    }

    //上传文章图片
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
    //科学喂养
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
    //快乐领养
    @GetMapping("/happy-adoption/list_query")
    @RequiresPermissions("article:all")
    @ResponseBody
    public Map<String, Object> happy_adoption_query(RequestParamsPetList params) {
        System.out.println(params);
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
        System.out.println(params);
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
        System.out.println(params);
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


    // feedback 列表
    @GetMapping("/feedback/list_query")
    @RequiresPermissions("feedback:all")
    @ResponseBody
    public Map<String, Object> feedbackList_query(RequestParamsPetList params) {
        System.out.println(params);
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
    //all, 审批列表查询
    @GetMapping("/all/list_query")
    @RequiresPermissions("approval:pet")
    @ResponseBody
    public Map<String, Object> allList_query(RequestParamsPetList params) {
        System.out.println(params);
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


    //删除文章
    @PostMapping("/delete")
    @RequiresPermissions("article:delete")
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

    //删除banner
    @PostMapping("/banner/delete")
    @RequiresPermissions("article:delete")
    @ResponseBody
    public ResultVO deleteBanner(int[] ids) {
        ResultVO resultVO = new ResultVO();
        int i = articleService.deleteBannerByIds(ids);
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
        System.out.println("id:"+id+"  status:"+status);
        if (status == ArticleStatus.PASS.getCode()) {
            int i = articleService.pass(id);
            if (i == 1) {
                resultVO.setCode(200);
                resultVO.setMsg("OK！");
            } else {
                resultVO.setCode(500);
                resultVO.setMsg("出现了错误！");
            }
        } else if (status == ArticleStatus.REJECT.getCode()) {
            int i = articleService.reject(id);
            if (i == 1) {
                resultVO.setCode(200);
                resultVO.setMsg("OK！");
            } else {
                resultVO.setCode(500);
                resultVO.setMsg("出现了错误！");
            }
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("Error！");
        }
        return resultVO;
    }




}