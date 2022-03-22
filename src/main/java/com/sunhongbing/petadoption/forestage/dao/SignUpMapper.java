package com.sunhongbing.petadoption.forestage.dao;

import com.sunhongbing.petadoption.backstage.entity.User;
import com.sunhongbing.petadoption.forestage.entity.SignUpParam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SignUpMapper {

    @Insert("insert into user(email,password,name,sex,age,tel,address,experience) " +
            "values(#{email},#{password},#{name},#{sex},#{age},#{tel},#{address},#{experience})")
    int insertUser(SignUpParam signUpParam);

    @Select("select * from user where email = #{email}")
    User selectUserByEmail(String email);
}
