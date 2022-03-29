package com.sunhongbing.petadoption.backstage.dao;

import com.sunhongbing.petadoption.backstage.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AdminMapper {
    // 通过username查找管理员信息
    @Select("select * from admin where username = #{username}")
    Admin findAdminByUsername(String username);

    //modify password
    @Update("update admin set password=#{password} where id=#{id}")
    int modifyPassword(int id, String password);
}
