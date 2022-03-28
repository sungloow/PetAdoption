package com.sunhongbing.petadoption.backstage.dao;

import com.sunhongbing.petadoption.backstage.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    // 通过username查找用户信息
    @Select("select * from user where email = #{email}")
    User findUserByEmail(String email);

    @Select("select * from user where id = #{id}")
    User getUserById(int id);
}
