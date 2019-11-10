package com.example.demo.service;

import com.example.demo.pojo.dto.User;
import com.example.demo.pojo.po.Permission;
import com.example.demo.pojo.po.Role;
import com.sun.istack.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    void deletePermissions(List<String> ids);

    List<com.example.demo.pojo.dto.Permission> getAllPermissionsTree();

    com.example.demo.pojo.dto.Permission addPermission(String name, String permission, @Nullable String parentId);

    Role addRole(String name, String description);

    List<com.example.demo.pojo.dto.Role> getRoles();

    void deleteRole(String id);


    Role editRole(String id, String name, String description, Boolean available, List<String> permissionIds);
}
