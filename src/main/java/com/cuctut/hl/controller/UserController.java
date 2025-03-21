package com.cuctut.hl.controller;

import com.cuctut.hl.common.RestResp;
import com.cuctut.hl.model.entity.User;
import com.cuctut.hl.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // 获取所有用户（仅管理员可访问）
    @GetMapping
    public RestResp<List<User>> getAllUsers() {
        return userService.getAllUsers();
    }

    // 删除用户（仅管理员可访问）
    @DeleteMapping("/{userId}")
    public RestResp<Void> deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId);
    }

    // 禁用启用用户（仅管理员）
    @PutMapping("/{userId}/{status}")
    public RestResp<Void> udtUserStatus(@PathVariable Long userId, @PathVariable Integer status) {
        return userService.udtUserStatus(userId, status);
    }
}
