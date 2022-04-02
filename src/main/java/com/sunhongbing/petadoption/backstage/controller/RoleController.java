package com.sunhongbing.petadoption.backstage.controller;

import com.github.pagehelper.PageHelper;
import com.sunhongbing.petadoption.backstage.entity.RequestParamsPetList;
import com.sunhongbing.petadoption.backstage.entity.SysMenu;
import com.sunhongbing.petadoption.backstage.entity.SysPermission;
import com.sunhongbing.petadoption.backstage.entity.SysRole;
import com.sunhongbing.petadoption.backstage.result.ResultVO;
import com.sunhongbing.petadoption.backstage.service.MenuService;
import com.sunhongbing.petadoption.backstage.service.PermissionService;
import com.sunhongbing.petadoption.backstage.service.RoleService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @className: RoleController
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-03-30 13:51
 */
@Controller
@RequestMapping("/admin")
public class RoleController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @GetMapping("/role/list_query")
    @ResponseBody
    @RequiresPermissions(value = {"root", "role:query"}, logical = Logical.OR)
    public Map<String, Object> roleList_query(RequestParamsPetList params) {
        PageHelper.startPage(params.getPageNumber(),params.getPageSize());
        List<SysRole> roleList = roleService.getAllRole(params.getSort(),params.getOrder());
        List<SysRole> roleList_size = roleService.getAllRole(params.getSort(),params.getOrder());
        int total = roleList_size.size();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", total);
        hashMap.put("rows", roleList);
        return hashMap;
    }
    @GetMapping("/role/detail/{id}")
    @RequiresPermissions(value = {"root", "role:query"}, logical = Logical.OR)
    public String roleDetail(Model model, @PathVariable int id) {
        SysRole role = roleService.getRoleById(id);
        //获取角色对应的菜单
        List<SysMenu> menuListByRole = menuService.getMenuListByRole(role.getRole());
        //获取角色对应的权限
        List<SysPermission> permissionListByRole = permissionService.getPermissionListByRoleId(id);

        model.addAttribute("role", role);
        model.addAttribute("menuList", menuListByRole);
        model.addAttribute("permissionList", permissionListByRole);
        return "backstage/html/menu/role-detail";
    }
    //role modify
    @GetMapping("/role/modify/{id}")
    @RequiresPermissions(value = {"root", "role:update"}, logical = Logical.OR)
    public String roleModify(Model model, @PathVariable int id) {
        SysRole role = roleService.getRoleById(id);
        //获取角色对应的菜单
        List<SysMenu> menuListByRole = menuService.getMenuListByRole(role.getRole());
        //取出id
        List<Integer> menuIdList = new ArrayList<>();
        for (SysMenu menu : menuListByRole) {
            menuIdList.add(menu.getId());
            if (menu.getChildMenu() != null) {
                for (SysMenu childMenu : menu.getChildMenu()) {
                    menuIdList.add(childMenu.getId());
                }
            }
        }
        //获取角色对应的权限
        List<SysPermission> permissionListByRole = permissionService.getPermissionListByRoleId(id);
        //取出id
        List<Integer> permissionIdList = new ArrayList<>();
        for (SysPermission permission : permissionListByRole) {
            permissionIdList.add(permission.getId());
        }

        //获取所有菜单
        List<SysMenu> menuList = menuService.getAllMenuList();
        //获取所有权限
        List<SysPermission> permissionList = permissionService.getAllPermission();
        model.addAttribute("role", role);
        model.addAttribute("menuList", menuList);
        model.addAttribute("permissionList", permissionList);
        model.addAttribute("menuIdList", menuIdList);
        model.addAttribute("permissionIdList", permissionIdList);
        return "backstage/html/menu/role-modify";
    }
    @PostMapping("/role/modify")
    @ResponseBody
    @RequiresPermissions(value = {"root", "role:update"}, logical = Logical.OR)
    public ResultVO roleModify_post(int id, @RequestParam List<Integer> menu_ids, @RequestParam List<Integer> permission_ids) {
        ResultVO vo = new ResultVO();
        int modifyRole = roleService.modifyRole(id, menu_ids, permission_ids);
        if (modifyRole == 1) {
            vo.setCode(200);
            vo.setMsg("修改成功");
        } else {
            vo.setCode(500);
            vo.setMsg("修改失败");
        }
        return vo;
    }
    //role add
    @GetMapping("/role/add")
    @RequiresPermissions(value = {"root", "role:insert"}, logical = Logical.OR)
    public String roleAdd(Model model) {
        //获取所有菜单
        List<SysMenu> menuList = menuService.getAllMenuList();
        //获取所有权限
        List<SysPermission> permissionList = permissionService.getAllPermission();
        model.addAttribute("menuList", menuList);
        model.addAttribute("permissionList", permissionList);
        return "backstage/html/menu/role-add";
    }
    @PostMapping("/role/add")
    @ResponseBody
    @RequiresPermissions(value = {"root", "role:insert"}, logical = Logical.OR)
    public ResultVO roleAdd_post(String description, String role, @RequestParam List<Integer> menu_ids, @RequestParam List<Integer> permission_ids) {
        ResultVO vo = new ResultVO();
        if ((description == null || description.equals("")) || (role == null || role.equals(""))) {
            vo.setCode(500);
            vo.setMsg("角色名称或描述不能为空");
            return vo;
        }
        //role只能是英文字母
        if (!role.matches("^[a-zA-Z]*$")) {
            vo.setCode(500);
            vo.setMsg("role 只能是英文字母");
            return vo;
        }
        SysRole sysRole = new SysRole();
        sysRole.setDescription(description);
        sysRole.setRole(role);
        int addRole = roleService.addRole(sysRole, menu_ids, permission_ids);
        if (addRole == 1) {
            vo.setCode(200);
            vo.setMsg("添加成功");
        } else if (addRole == -1) {
            vo.setCode(500);
            vo.setMsg("角色已存在");
        } else {
            vo.setCode(500);
            vo.setMsg("添加失败");
        }
        return vo;
    }
    //role delete
    @PostMapping("/role/delete")
    @ResponseBody
    @RequiresPermissions(value = {"root", "role:delete"}, logical = Logical.OR)
    public ResultVO roleDelete(@RequestParam List<Integer> ids) {
        ResultVO resultVO = new ResultVO();
        int i = roleService.deleteRoles(ids);
        if (i == 1) {
            resultVO.setCode(200);
            resultVO.setMsg("删除成功！");
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("删除失败！");
        }
        return resultVO;
    }
}
