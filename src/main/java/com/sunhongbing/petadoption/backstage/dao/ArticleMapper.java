package com.sunhongbing.petadoption.backstage.dao;

import com.sunhongbing.petadoption.backstage.entity.Article;
import com.sunhongbing.petadoption.backstage.entity.Banner;
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

    // queryBanners
    @Select("select * from banner  order by ${order} ${sort}")
    List<Banner> queryBanners(String order, String sort);


    //新增一个文章
    @Insert("insert into article (author,title,content,type) values (#{author},#{title},#{content},#{type})")
    int addArticle(Article article);

    //新增一个 banner
    @Insert("insert into banner (author,title,content,cover) values (#{author},#{title},#{content},#{cover})")
    int addBanner(Banner banner);

    //根据文章id查询文章
    @Select("select * from article where id = #{id}")
    Article getArticleById(int id);

    //banner query by id
    @Select("select * from banner where id = #{id}")
    Banner getBannerById(int id);

    //根据文章id删除文章
    @Delete("delete from article where id = #{id}")
    int deleteArticleById(int id);

    //批量删除文章
    @Delete("<script>" +
            "delete from article where id in \n" +
            "<foreach collection='array' item='id' open='(' separator=',' close=')'>#{id}</foreach>\n" +
            "</script>")
    int deleteArticleByIds(int[] ids);

    //批量删除banner
    @Delete("<script>" +
            "delete from banner where id in \n" +
            "<foreach collection='array' item='id' open='(' separator=',' close=')'>#{id}</foreach>\n" +
            "</script>")
    int deleteBannerByIds(int[] ids);

    //修改文章 Status
    @Update("update article set status = #{status} where id = #{id}")
    int updateArticleStatus(int id, int status);

    //查询所有文章,by types
    @Select("<script>" +
            "select * from article where type in \n" +
            "<foreach collection='types' item='type' open='(' separator=',' close=')'>#{type}</foreach>\n" +
            "<if test=\"status != -99 \">"
            + "and status = #{status}"
            + "</if>"
            + "order by ${order} ${sort}"+
            "</script>")
    List<Article> queryArticlesByTypes(@Param("types") int[] types, int status, String order, String sort);
}
