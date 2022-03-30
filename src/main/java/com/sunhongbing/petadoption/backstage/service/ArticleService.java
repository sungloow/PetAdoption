package com.sunhongbing.petadoption.backstage.service;

import com.sunhongbing.petadoption.backstage.entity.Article;
import com.sunhongbing.petadoption.backstage.entity.Banner;
import com.sunhongbing.petadoption.forestage.entity.ApplyRecord;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ArticleService {

    //查询所有的文章
    List<Article> queryArticles(int type, int status, String order, String sort);

    // query banner
    List<Banner> queryBanners(String order, String sort);


    //新增一个文章
    int addArticle(Article article);

    int addBanner(Banner banner);

    //根据文章id查询文章
    Article getArticleById(int id);

    // query banner
    Banner getBannerById(int id);

    //根据文章id删除文章
    int deleteArticleById(int id);

    //批量删除文章
    int deleteArticleByIds(int[] ids);

    //批量删除banner
    int deleteBannerByIds(int[] ids);

    //pass
    int pass(int id);

    //reject
    int reject(int id);

    List<Article> queryArticlesByTypes(int[] types, int status, String order, String sort);


}
