package com.sunhongbing.petadoption.backstage.dao;

import com.sunhongbing.petadoption.backstage.entity.AdminInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminInfoMapper {
    // 通过username查找用户信息
    @Select("select * from admin where username = #{username}")
    AdminInfo findAdminByUsername(String username);
}
