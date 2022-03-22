package com.sunhongbing.petadoption.forestage.dao;

import com.sunhongbing.petadoption.backstage.entity.Animal;
import org.apache.ibatis.annotations.*;
import org.springframework.lang.Nullable;

import java.util.List;

@Mapper
public interface AdoptionMapper {
    @Select("<script>"
            + "select * from animal"
            + "<if test=\"status != -99 \">"
            + "where status = #{status}"
            + "</if>"
            + "</script>")
    List<Animal> findAll(int status);

    List<Animal> findPetByStatusAndType(String status, String type);

    @Select("select * from animal where id = #{id}")
    Animal findPetById(String id);

    @Update("update animal set name=#{name}, sex=#{sex}, type=#{type}, birth=#{birth}, detail=#{detail}, status=#{status} where id = #{id} ")
    int updatePet(Animal animal);

    @Insert("insert into animal( name, sex, type, birth, detail) " +
            "values(#{name}, #{sex}, #{type}, #{birth}, #{detail} )")
    int insertPet(Animal animal);

    @Delete("delete from animal where id = #{id}")
    int deletePet(String id);

    int deletePets(String[] ids);
}
