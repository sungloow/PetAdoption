package com.sunhongbing.petadoption.backstage.service.impl;

import com.sunhongbing.petadoption.backstage.dao.AdminManageMapper;
import com.sunhongbing.petadoption.backstage.dao.AdminMapper;
import com.sunhongbing.petadoption.backstage.dao.RoleMapper;
import com.sunhongbing.petadoption.backstage.entity.Admin;
import com.sunhongbing.petadoption.backstage.entity.SysRole;
import com.sunhongbing.petadoption.backstage.enums.UserStatus;
import com.sunhongbing.petadoption.backstage.service.AdminManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.DigestUtils;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * @className: AdminManageServiceImpl
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-03-29 13:20
 */
@Service
public class AdminManageServiceImpl implements AdminManageService {
    @Autowired
    private AdminManageMapper adminManageMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Admin> queryAllUsers(int status, String order, String sort) {
        return adminManageMapper.selectAllAdmin(status, order, sort);
    }

    @Override
    public Admin queryUserById(int id) {
        return adminManageMapper.selectAdminById(id);
    }

    @Override
    public int deleteUserById(int id) {
        return adminManageMapper.updateAdminStatusById(id, UserStatus.DELETE.getCode());
    }

    @Override
    public int deleteUserByIds(List<Integer> ids) {
        return adminManageMapper.updateAdminStatusByIds(ids, UserStatus.DELETE.getCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addUser(Admin admin, List<Integer> roleIds) {
        //用户名只能是字母、数字、下划线组成
        String username = admin.getUsername();
        if (!username.matches("^[a-zA-Z0-9_]{1,}$")) {
            return -1;
        }
        String md5Password = DigestUtils.md5DigestAsHex(admin.getUsername().getBytes());
        admin.setPassword(md5Password);
        try {
            adminManageMapper.addAdmin(admin);
            if (roleIds != null) {
                Admin newAdmin = adminMapper.findAdminByUsername(admin.getUsername());
                adminManageMapper.bindRoles(roleIds, newAdmin.getId());
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            //判断是否是主键重复导致的异常
            if (e.getMessage().contains("Duplicate entry")){
                return -2;
            } else {
                return -3;
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int modifyUser(Admin admin, List<Integer> roleIds) {
        //用户名只能是字母、数字、下划线组成
        String username = admin.getUsername();
        if (!username.matches("^[a-zA-Z0-9_]{1,}$")) {
            return -1;
        }
        try {
            List<SysRole> oldRoleIds = roleMapper.getRoleListById(admin.getId());
            List<Integer> oldIds = new ArrayList<>(oldRoleIds.size());
            for (SysRole role : oldRoleIds) {
                oldIds.add(role.getId());
            }
            if (!oldIds.equals(roleIds)) {
                if (oldIds.size() > 0) {
                    //删除旧的角色
                    adminManageMapper.unbindRoles(oldIds, admin.getId());
                }
                if (roleIds.size() > 0) {
                    //绑定新的角色
                    adminManageMapper.bindRoles(roleIds, admin.getId());
                }
            }
            return adminManageMapper.updateAdminInfo(admin);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            //判断是否是主键重复导致的异常
            if (e.getMessage().contains("Duplicate entry")){
                return -2;
            } else {
                return -3;
            }
        }
    }

    @Transactional(isolation = Isolation.DEFAULT)
    @Override
    public int disableUser(List<Integer> ids) {
        return adminManageMapper.updateAdminStatusByIds(ids, UserStatus.DISABLE.getCode());
    }

    @Override
    public int enableUser(List<Integer> ids) {
        return adminManageMapper.updateAdminStatusByIds(ids, UserStatus.NORMAL.getCode());
    }

    @Override
    public int bindRole(int roleId, int adminId) {
        return adminManageMapper.bindRole(roleId, adminId);
    }

    @Override
    public int bindRoles(List<Integer> roleId, int adminId) {
        return adminManageMapper.unbindRoles(roleId, adminId);
    }

    @Override
    public int unbindRole(int roleId, int adminId) {
        return adminManageMapper.unbindRole(roleId, adminId);
    }

    @Transactional(isolation = Isolation.DEFAULT)
    @Override
    public int unbindRoles(List<Integer> roleId, int adminId) {
        return adminManageMapper.unbindRoles(roleId, adminId);
    }

    @Override
    public int resetPassword(List<Integer> ids) {
        String password = "123456789";
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        return adminManageMapper.resetPasswords(ids, md5Password);
    }
}
