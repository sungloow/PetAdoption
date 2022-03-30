package com.sunhongbing.petadoption.backstage.service;

import com.sunhongbing.petadoption.backstage.entity.Admin;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AdminManageService {
    // 查询所有用户
    List<Admin> queryAllUsers(int status, String order, String sort);

    // 根据用户id查询用户
    Admin queryUserById(int id);

    // 根据用户id删除用户
    int deleteUserById(int id);
    //批量删除用户
    int deleteUserByIds(List<Integer> ids);

    // 添加用户
    int addUser(Admin admin, List<Integer> roleIds);

    // 修改用户
    int modifyUser(Admin admin, List<Integer> roleIds);

    //禁用用户
    int disableUser(List<Integer> ids);

    //启用用户
    int enableUser(List<Integer> ids);

    //用户绑定角色
    int bindRole(int roleId, int adminId);

    //用户批量绑定角色
    int bindRoles(List<Integer> roleId, int adminId);

    //用户解绑角色
    int unbindRole(int roleId, int adminId);

    //用户批量解绑角色
    int unbindRoles(List<Integer> roleId, int adminId);

    //重置密码
    int resetPassword(List<Integer> ids);
}
