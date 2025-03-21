package com.cuctut.hl.controller;

import com.cuctut.hl.auth.UserHolder;
import com.cuctut.hl.common.RestResp;
import com.cuctut.hl.model.dto.req.UserInfoUpdateReqDto;
import com.cuctut.hl.model.dto.req.UserLoginReqDto;
import com.cuctut.hl.model.dto.req.UserRegisterReqDto;
import com.cuctut.hl.model.dto.resp.UserLoginRespDto;
import com.cuctut.hl.model.dto.resp.UserRegisterRespDto;
import com.cuctut.hl.model.entity.User;
import com.cuctut.hl.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public RestResp<UserRegisterRespDto> register(@Valid @RequestBody UserRegisterReqDto dto) {
        return userService.register(dto);
    }

    @PostMapping("/login")
    public RestResp<UserLoginRespDto> login(@Valid @RequestBody UserLoginReqDto dto) {
        return userService.login(dto);
    }

    @GetMapping("/userInfo")
    public RestResp<User> getUserInfo() {
        return userService.getUserInfo();
    }

    @PutMapping("/userInfo")
    public RestResp<Void> udtUserInfo(@Valid @RequestBody UserInfoUpdateReqDto dto) {
        return userService.udtUserInfo(dto);
    }
}
