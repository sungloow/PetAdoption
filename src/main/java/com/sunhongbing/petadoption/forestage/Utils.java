package com.sunhongbing.petadoption.forestage;

import com.sunhongbing.petadoption.backstage.entity.Article;
import com.sunhongbing.petadoption.backstage.enums.ArticleType;

import java.util.List;

/**
 * @className: Utils
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-03-29 01:31
 */
public class Utils {
    //判断 List<Article> 是否为空
    public static Article handlerAboutUsArticle(List<Article> articles) {
        if (articles.size() > 0) {
            return articles.get(0);
        } else {
            Article article = new Article();
            article.setTitle("");
            article.setContent("");
            article.setType(ArticleType.ADOPTION_METHOD.getCode());
            return article;
        }
    }
}
