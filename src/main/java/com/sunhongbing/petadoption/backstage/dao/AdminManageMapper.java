package com.sunhongbing.petadoption.backstage.dao;

import com.sunhongbing.petadoption.backstage.entity.Admin;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminManageMapper {
    //查询管理员信息
    @Select("select * from admin where id=#{id}")
    Admin selectAdminById(int id);

    //查询所有管理员信息
    @Select("<script>"
            + "select * from admin where status != -1"
            + "<if test=\"status != -99 \">"
            + "and status = #{status}"
            + "</if>"
            + "order by ${order} ${sort}"
            + "</script>")
    List<Admin> selectAllAdmin(int status, String order, String sort);

    //重置密码
    @Update("update admin set password=#{password} where username=#{username}")
    int resetPassword(String password, String adminId);
    // 批量重置密码
    //批量修改管理员账号状态
    @Update("<script>"
            + "update admin set password=#{password} where id in"
            + "<foreach collection='ids' item='id' open='(' separator=',' close=')'>"
            + "#{id}"
            + "</foreach>"
            + "</script>")
    int resetPasswords(@Param("ids") List<Integer> ids, String password);

    //添加管理员
    @Insert("insert into admin(username,password,name,sex,tel,email,address) values" +
            "(#{username}, #{password}, #{name}, #{sex}, #{tel}, #{email}, #{address})")
    int addAdmin(Admin admin);

    //修改管理员账号状态
    @Update("update admin set status=#{status} where id=#{id}")
    int updateAdminStatusById(int id, int status);
    //批量修改管理员账号状态
    @Update("<script>"
            + "update admin set status=#{status} where id in"
            + "<foreach collection='ids' item='id' open='(' separator=',' close=')'>"
            + "#{id}"
            + "</foreach>"
            + "</script>")
    int updateAdminStatusByIds(@Param("ids") List<Integer> ids, int status);

    //修改管理员信息
    @Update("update admin set username=#{username}, name=#{name}, " +
            "sex=#{sex}, tel=#{tel}, email=#{email}, address=#{address} " +
            "where id = #{id} ")
    int updateAdminInfo(Admin admin);

    //用户绑定角色
    @Insert("INSERT INTO role_admin_ref(role_id, admin_id) VALUES(#{roleId},#{adminId})")
    int bindRole(int roleId, int adminId);
    //用户批量绑定角色
    @Insert("<script>" +
            "insert into role_admin_ref (role_id, admin_id) values " +
            "<foreach collection='roleId' item='item'  index='index' separator=',' >" +
            " ( #{item}, #{adminId} ) " +
            "</foreach> " +
            "</script>")
    int bindRoles(@Param("roleId") List<Integer> roleId, int adminId);

    //用户解绑角色
    @Delete("DELETE FROM role_admin_ref WHERE role_id = #{roleId} AND admin_id = #{adminId}")
    int unbindRole(int roleId, int adminId);
    //用户批量解绑角色
    @Delete("<script>" +
            "delete from role_admin_ref where role_id in " +
            "<foreach collection='roleId' item='item'  index='index' separator=',' >" +
            " ( #{item} ) " +
            "</foreach> " +
            "and admin_id = #{adminId} " +
            "</script>")
    int unbindRoles(@Param("roleId") List<Integer> roleId, int adminId);

}
