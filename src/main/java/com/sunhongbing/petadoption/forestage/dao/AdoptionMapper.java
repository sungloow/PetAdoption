package com.sunhongbing.petadoption.forestage.dao;

import com.sunhongbing.petadoption.backstage.entity.Animal;
import com.sunhongbing.petadoption.forestage.entity.ApplyRecord;
import org.apache.ibatis.annotations.*;

import java.sql.Blob;
import java.util.List;

@Mapper
public interface AdoptionMapper {
    @Select("<script>"
            + "select * from animal"
            + "<if test=\"status != -99 \">"
            + "where status = #{status}"
            + "</if>"
            + "order by ${order} ${sort}"
            + "</script>")
    List<Animal> findAll(int status, String order, String sort);


    List<Animal> findPetByStatusAndType(String status, String type);

    @Select("select * from animal where id = #{id}")
    Animal findPetById(int id);

    @Update("update animal set name=#{name}, sex=#{sex}, type=#{type}, birth=#{birth}, detail=#{detail}, status=#{status} where id = #{id} ")
    int updatePet(Animal animal);

    @Insert("insert into animal( name, sex, type, birth, detail, pics) " +
            "values(#{name}, #{sex}, #{type}, #{birth}, #{detail}, #{pics} )")
    int insertPet(Animal animal);

    @Update("update animal set pics = #{pic} where id = #{id}")
    int insertPetPic(int id, String pic);

    //查找图片
    @Select("select pics from animal where id = #{id}")
    String findPetPicById(int id);

    @Delete("delete from animal where id = #{id}")
    int deletePet(int id);

    //批量删除
    @Delete("<script>" +
            "delete from animal where id in \n" +
            "<foreach collection='array' item='id' open='(' separator=',' close=')'>#{id}</foreach>\n" +
            "</script>")
    int deletePets(int[] ids);


    //is_applied
    //查询结果可能为空，需要判断
    @Select("select * from animal_adoption where petId = #{petId} and userId = #{userId}")
    ApplyRecord isApplied(int userId, int petId);

    @Insert("insert into animal_adoption(petId, userId, status) values(#{petId}, #{userId}, #{status})")
    int apply(int userId, int petId, int status);

    @Update("update animal_adoption set status = #{status} where petId = #{petId} and userId = #{userId}")
    int update_adoption_status(int userId, int petId, int status);

    @Delete("delete from animal_adoption where petId = #{petId} and userId = #{userId}")
    int cancel_adoption(int userId, int petId);

    @Update("update animal set status = #{status} where id = #{id}")
    int updatePetStatus(int id, int status);


}
