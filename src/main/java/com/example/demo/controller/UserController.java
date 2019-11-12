package com.example.demo.controller;


import com.example.demo.config.shiro.MyShiroRealm;
import com.example.demo.expection.BusinessException;
import com.example.demo.expection.BusinessExceptionEnum;
import com.example.demo.pojo.dto.Permission;
import com.example.demo.pojo.dto.Token;
import com.example.demo.pojo.dto.User;
import com.example.demo.pojo.po.Role;
import com.example.demo.pojo.vo.Result;
import com.example.demo.service.UserService;
import com.github.dozermapper.core.Mapper;
import com.sun.istack.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController

public class UserController {

    @Autowired
    UserService userService;

    @Resource
    private Mapper dozerMapper;

    @PostMapping("/login")
    public Result<Token> login(@RequestParam String user, @RequestParam String password) {
        UsernamePasswordToken token = new UsernamePasswordToken(user, password);
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.login(token);
        return Result.success(new Token(currentUser.getSession().getId().toString()));
    }

    @PutMapping("/users/{userId}/roles")
    @RequiresPermissions("user:role:update")
    public Result<User> editUserRole(@PathVariable String userId, @RequestParam List<String> roleIds) {
        return Result.success(userService.editUserRole(userId, roleIds));
    }


    @GetMapping("/users/{id}")
    public Result<User> getUser(@PathVariable String id) {
        com.example.demo.pojo.po.User currentUser = (com.example.demo.pojo.po.User) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
        if (Long.parseLong(id) == (currentUser.getId())) {
            return getCurrentUser();
        } else if (SecurityUtils.getSubject().isPermitted("user:view")) {
            return Result.success(userService.getUser(id));
        } else {
            throw new BusinessException(BusinessExceptionEnum.COMMON_HAS_NO_PERMISSION);
        }

    }

    @GetMapping("/currentUser")
    public Result<User> getCurrentUser() {
        com.example.demo.pojo.po.User currentUser = (com.example.demo.pojo.po.User) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
        User userDto = dozerMapper.map(currentUser, User.class);
        return Result.success(userDto);
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        SecurityUtils.getSubject().logout();
        return Result.success();
    }

    @GetMapping("/unauth")
    public void unLogin() {
        throw new UnauthenticatedException();
    }


    @GetMapping("/users")
    @RequiresPermissions("role:view")
    public Result<List<User>> getUserList() {
        return Result.success(userService.getAllUsers());
    }


    @PostMapping("/roles/")
    @RequiresPermissions("role:add")
    public Result<Role> addRole(@RequestParam String name, @RequestParam String desc, @RequestParam String[] permissionIds) {
        return Result.success(userService.addRole(name, desc, Arrays.asList(permissionIds)));
    }


    @DeleteMapping("/roles/{id}")
    @RequiresPermissions("role:delete")
    public Result<Void> deleteRole(@PathVariable String id) {
        userService.deleteRole(id);
        return Result.success();
    }

    @GetMapping("/roles/")
    @RequiresPermissions("role:view")
    public Result<List<Role>> getRoles() {
        return Result.success(userService.getRoles());

    }

    @PutMapping("/roles/{id}")
    @RequiresPermissions("role:update")
    public Result<Role> editRole(@PathVariable String id,
                                 @RequestParam String name,
                                 @RequestParam String desc,
                                 @RequestParam Boolean available,
                                 @RequestParam String[] permissionIds) {

        return Result.success(userService.editRole(id, name, desc, available, Arrays.asList(permissionIds)));
    }

    @GetMapping("/permissions/")
    @RequiresPermissions("permission:view")
    public Result<List<Permission>> getAllPermisstionsTree() {
        return Result.success(userService.getAllPermissionsTree());
    }

    @PostMapping("/permissions/")
    @RequiresPermissions("permission:add")
    public Result<Permission> addPermisstion(@RequestParam String name,
                                             @RequestParam String permission,
                                             @RequestParam(required = false) String parentId) {
        return Result.success(userService.addPermission(name, permission, parentId));
    }

    @DeleteMapping("/permissions/{id}")
    @RequiresPermissions("permission:delete")
    public Result<Void> deletePermisstions(@PathVariable String id) {
        userService.deletePermissions(id);
        return Result.success();
    }
}
