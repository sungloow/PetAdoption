package com.sunhongbing.petadoption.backstage.service.impl;

import com.sunhongbing.petadoption.backstage.dao.ArticleMapper;
import com.sunhongbing.petadoption.backstage.entity.Article;
import com.sunhongbing.petadoption.backstage.enums.ArticleStatus;
import com.sunhongbing.petadoption.backstage.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.Action;
import java.util.List;

/**
 * @className: ArticleServiceImpl
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-03-27 21:19
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<Article> queryArticles(int type, int status, String order, String sort) {
        return articleMapper.queryArticles(type, status, order, sort);
    }

    @Override
    public List<Article> queryArticles(int status, String order, String sort) {
        return articleMapper.queryArticlesNoType(status, order, sort);
    }

    @Override
    public int addArticle(Article article) {
        return articleMapper.addArticle(article);
    }

    @Override
    public Article getArticleById(int id) {
        return articleMapper.getArticleById(id);
    }

    @Override
    public int deleteArticleById(int id) {
        return articleMapper.deleteArticleById(id);
    }

    @Override
    public int deleteArticleByIds(int[] ids) {
        return articleMapper.deleteArticleByIds(ids);
    }

    @Override
    public int pass(int id) {
        return articleMapper.updateArticleStatus(id, ArticleStatus.PASS.getCode());
    }

    @Override
    public int reject(int id) {
        return articleMapper.updateArticleStatus(id, ArticleStatus.REJECT.getCode());
    }
}
