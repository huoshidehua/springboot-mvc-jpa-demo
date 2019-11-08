package com.example.demo.service;

import com.example.demo.pojo.dto.Token;
import com.example.demo.pojo.dto.User;
import com.example.demo.pojo.po.Permission;
import com.example.demo.pojo.po.Role;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    void addRole(Role role);

    void editRole(Role role);

    void delRole(Long roleId);

    void addPermission(Permission permission);
}
