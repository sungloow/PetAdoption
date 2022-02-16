package com.sunhongbing.petadoption.backstage.dao;

import com.sunhongbing.petadoption.backstage.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {
    // 通过username查找管理员信息
    @Select("select * from admin where username = #{username}")
    Admin findAdminByUsername(String username);
}
