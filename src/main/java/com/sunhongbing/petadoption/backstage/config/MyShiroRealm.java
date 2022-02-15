package com.sunhongbing.petadoption.backstage.config;

import com.sunhongbing.petadoption.backstage.entity.AdminInfo;
import com.sunhongbing.petadoption.backstage.entity.SysPermission;
import com.sunhongbing.petadoption.backstage.entity.SysRole;
import com.sunhongbing.petadoption.backstage.enums.UserStatus;
import com.sunhongbing.petadoption.backstage.service.AdminInfoService;
import com.sunhongbing.petadoption.backstage.service.PermissionService;
import com.sunhongbing.petadoption.backstage.service.RoleService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    private AdminInfoService adminInfoService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    //主要是用来进行身份认证的，验证用户输入的账号和密码是否正确
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        System.out.println("MyShiroRealm.doGetAuthenticationInfo() 身份认证");
        //获取用户的输入的账号
        String username = (String)token.getPrincipal();
        System.out.println(token.getCredentials());
        //通过username从数据库中查找 User对象
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        AdminInfo adminInfo = adminInfoService.findAdminByUsername(username);
        System.out.println("----->>adminInfo="+adminInfo);
        if(adminInfo == null){
            return null;
        }
        if (UserStatus.DISABLE.getCode()==adminInfo.getStatus()) {
            throw new LockedAccountException(username + "账号被锁定，请联系管理员！");
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                adminInfo.getId(), //用户名
                adminInfo.getPassword(), //密码
                getName()  //realm name
        );
        return authenticationInfo;
    }

    //主要是对用户角色权限进行控制
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

//        AdminInfo adminInfo  = (AdminInfo) principals.getPrimaryPrincipal();
        String id = (String) principals.getPrimaryPrincipal();
        List<SysRole> roleList = roleService.getRoleListByAdminId(id);
        System.out.println("----->>roleList="+roleList);
        for(SysRole role:roleList){
            authorizationInfo.addRole(role.getRole());
            List<SysPermission> permissionListByRoleId = permissionService.getPermissionListByRoleId(role.getId());
            System.out.println("----->>permissionListByRoleId="+permissionListByRoleId);
            for(SysPermission p:permissionListByRoleId){
                authorizationInfo.addStringPermission(p.getPermission());
            }
        }
        return authorizationInfo;
    }
}