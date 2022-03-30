package com.sunhongbing.petadoption.backstage.dao;

import com.sunhongbing.petadoption.backstage.entity.SysRole;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleMapper {

    // 根据用户id查询角色
    @Select("SELECT * FROM role WHERE id IN (SELECT role_id FROM role_admin_ref WHERE admin_id = #{id})")
    List<SysRole> getRoleListById(int id);


    //SysRole getRoleById(int id)
    @Select("SELECT * FROM role WHERE id = #{id} ")
    SysRole getRoleById(int id);

    // getRoleByRole
    @Select("SELECT * FROM role WHERE role = #{role} ")
    SysRole getRoleByRole(String role);

    // 获取所有角色
    @Select("SELECT * FROM role where role != 'root' and role != 'user' order by ${order} ${sort} ")
    List<SysRole> getAllRole(String order, String sort);

    //添加角色
    @Insert("INSERT INTO role(description, role) VALUES(#{description},#{role})")
    int addRole(SysRole role);

    //删除角色
    @Delete("DELETE FROM role WHERE id = #{id}")
    int deleteRole(int id);
    //批量删除角色
    @Delete("<script>" +
            "delete from role where id in " +
            "<foreach collection='ids' item='id' index='index' open='(' separator=',' close=')' >" +
            " #{id} " +
            "</foreach> " +
            "</script>")
    int deleteRoles(@Param(value="ids") List<Integer> ids);

    //删除角色与菜单的关联
    @Delete("<script>" +
            "delete from role_menu_ref where role_id in " +
            "<foreach collection='ids' item='id' index='index'  open='(' separator=',' close=')'  >" +
            " #{id} " +
            "</foreach> " +
            "</script>")
    int deleteRoleMenus(@Param(value="ids") List<Integer> ids);
    //删除角色与权限的关联
    @Delete("<script>" +
            "delete from role_permission_ref where role_id in " +
            "<foreach collection='ids' item='id' index='index' open='(' separator=',' close=')' >" +
            " #{id} " +
            "</foreach> " +
            "</script>")
    int deleteRolePermissions(@Param(value="ids") List<Integer> ids);
    //删除角色与用户的关联
    @Delete("<script>" +
            "delete from role_admin_ref where role_id in " +
            "<foreach collection='ids' item='id' index='index' open='(' separator=',' close=')' >" +
            " #{id} " +
            "</foreach> " +
            "</script>")
    int deleteRoleUsers(@Param(value="ids") List<Integer> ids);

    //角色绑定权限
    @Insert("INSERT INTO role_permission_ref(role_id, permission_id) VALUES(#{roleId},#{permissionId})")
    int bindPermission(int roleId, int permissionId);
    //角色批量绑定权限
    @Insert("<script>" +
            "insert into role_permission_ref (role_id, permission_id) values " +
            "<foreach collection='permissionId' item='item'  index='index' separator=',' >" +
            " (#{roleId},#{item}) " +
            "</foreach> " +
            "</script>")
    int bindPermissions(int roleId, @Param(value="permissionId") List<Integer> permissionId);
    //角色解绑权限
    @Delete("DELETE FROM role_permission_ref WHERE role_id = #{roleId} AND permission_id = #{permissionId}")
    int unbindPermission(int roleId, int permissionId);
    //角色批量解绑权限
    @Delete("<script>" +
            "delete from role_permission_ref where role_id = #{roleId} and permission_id in " +
            "<foreach collection='permissionId' item='item'  index='index' open='(' separator=',' close=')' >" +
            " #{item} " +
            "</foreach> " +
            "</script>")
    int unbindPermissions(int roleId, @Param(value="permissionId") List<Integer> permissionId);


    //角色添加菜单
    @Insert("INSERT INTO role_menu_ref(role_id, menu_id) VALUES(#{roleId},#{menuId})")
    int bindMenu(int roleId, int menuId);
    //角色批量添加菜单
    @Insert("<script>" +
            "insert into role_menu_ref (role_id, menu_id) values " +
            "<foreach collection='menuId' item='menu_id' index='index'  separator=',' >" +
            " (#{roleId},#{menu_id}) " +
            "</foreach> " +
            "</script>")
    int bindMenus(int roleId, @Param(value="menuId") List<Integer> menuId);
    //角色解绑菜单
    @Delete("DELETE FROM role_menu_ref WHERE role_id = #{roleId} AND menu_id = #{menuId}")
    int unbindMenu(int roleId, int menuId);
    //角色批量解绑菜单
    @Delete("<script>" +
            "delete from role_menu_ref where role_id = #{roleId} and menu_id in " +
            "<foreach collection='menuId' item='menuId' index='index' open='(' separator=',' close=')' >" +
            " #{menuId} " +
            "</foreach> " +
            "</script>")
    int unbindMenus(int roleId, @Param(value="menuId") List<Integer> menuId);



    //根据角色ID查询关联的菜单ID
    @Select("SELECT menu_id FROM role_menu_ref WHERE role_id = #{roleId}")
    List<Integer> getMenuIdByRoleId(int roleId);
    //根据角色ID查询关联的权限ID
    @Select("SELECT permission_id FROM role_permission_ref WHERE role_id = #{roleId}")
    List<Integer> getPermissionIdByRoleId(int roleId);

    //获取最大的roleId
    @Select("SELECT MAX(id) FROM role")
    int getMaxRoleId();

}
