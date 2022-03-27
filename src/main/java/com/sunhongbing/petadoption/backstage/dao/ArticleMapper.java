package com.sunhongbing.petadoption.backstage.dao;

import com.sunhongbing.petadoption.backstage.entity.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    //查询所有的文章
    @Select("<script>"
            + "select * from article where type = #{type}"
            + "<if test=\"status != -99 \">"
            + "and status = #{status}"
            + "</if>"
            + "order by ${order} ${sort}"
            + "</script>")
    List<Article> queryArticles(int type, int status, String order, String sort);

    //查询所有的文章 不要type
    @Select("<script>"
            + "select * from article"
            + "<if test=\"status != -99 \">"
            + "where status = #{status}"
            + "</if>"
            + "order by ${order} ${sort}"
            + "</script>")
    List<Article> queryArticlesNoType(int status, String order, String sort);



    //新增一个文章
    @Insert("insert into article (author,title,content,type) values (#{author},#{title},#{content},#{type})")
    int addArticle(Article article);

    //根据文章id查询文章
    @Select("select * from article where id = #{id}")
    Article getArticleById(int id);

    //根据文章id删除文章
    @Delete("delete from article where id = #{id}")
    int deleteArticleById(int id);

    //批量删除文章
    @Delete("<script>" +
            "delete from article where id in \n" +
            "<foreach collection='array' item='id' open='(' separator=',' close=')'>#{id}</foreach>\n" +
            "</script>")
    int deleteArticleByIds(int[] ids);

    //修改文章 Status
    @Update("update article set status = #{status} where id = #{id}")
    int updateArticleStatus(int id, int status);

}
