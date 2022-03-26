package com.sunhongbing.petadoption.forestage.dao;

import com.sunhongbing.petadoption.backstage.entity.Animal;
import com.sunhongbing.petadoption.backstage.entity.User;
import com.sunhongbing.petadoption.forestage.entity.ApplyAnimal;
import com.sunhongbing.petadoption.forestage.entity.EditParam;
import com.sunhongbing.petadoption.forestage.entity.EditPasswordParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PersonalMapper {
    // 查询用户信息
    @Select("select * from user where id = #{id}")
    User queryUserInfoById(Integer id);

    // 修改用户信息
//
    @Update("update user set name = #{name}, sex=#{sex}, age=#{age}, tel=#{tel}," +
            " address = #{address}, experience=#{experience} where id = #{id}")
    int updateUserInfo(EditParam param);

    @Update("update user set password = #{newPwd} where id = #{id}")
    int updateUserPwd(EditPasswordParam param);

    // getOldPwd
    @Select("select password from user where id = #{id}")
    String getOldPwd(Integer id);

    //queryAdoptList
    @Select("SELECT aa.status, aa.time, a.id, a.name, a.sex, a.type, a.birth, a.detail, a.pics " +
            "FROM animal_adoption aa INNER JOIN animal a ON petId=id WHERE userId =  #{id}")
    List<ApplyAnimal> queryAdoptList(Integer id);
}
