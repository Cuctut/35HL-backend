package com.cuctut.hl.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuctut.hl.auth.JwtUtils;
import com.cuctut.hl.auth.UserHolder;
import com.cuctut.hl.common.ErrorCodeEnum;
import com.cuctut.hl.common.RestResp;
import com.cuctut.hl.exception.BusinessException;
import com.cuctut.hl.mapper.UserMapper;
import com.cuctut.hl.model.dto.req.UserInfoUpdateReqDto;
import com.cuctut.hl.model.dto.req.UserLoginReqDto;
import com.cuctut.hl.model.dto.req.UserRegisterReqDto;
import com.cuctut.hl.model.dto.resp.UserLoginRespDto;
import com.cuctut.hl.model.dto.resp.UserRegisterRespDto;
import com.cuctut.hl.model.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    public RestResp<UserRegisterRespDto> register(UserRegisterReqDto dto) {

        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername());
        if (count(query) > 0) throw new BusinessException(ErrorCodeEnum.USER_NAME_EXIST);

        String password = DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8));
        User user = User.builder()
                .username(dto.getUsername())
                .password(password)
                .role("USER")
                .build();
        save(user);

        return RestResp.ok(
                UserRegisterRespDto.builder()
                        .token(JwtUtils.generateToken(user))
                        .uid(user.getId())
                        .build()
        );
    }

    public RestResp<UserLoginRespDto> login(UserLoginReqDto dto) {

        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername());
        User user = getOne(query);
        if (Objects.isNull(user)) throw new BusinessException(ErrorCodeEnum.USER_ACCOUNT_NOT_EXIST);
        if (user.getStatus() == 0) throw new BusinessException(ErrorCodeEnum.USER_ACCOUNT_DISABLED);

        String password = DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8));
        if (!Objects.equals(user.getPassword(), password)) throw new BusinessException(ErrorCodeEnum.USER_PASSWORD_ERROR);

        return RestResp.ok(
                UserLoginRespDto.builder()
                        .token(JwtUtils.generateToken(user))
                        .uid(user.getId())
                        .userName(user.getUsername())
                        .role(user.getRole())
                        .build()
        );
    }

    public RestResp<User> getUserInfo() {
        Long userId = UserHolder.getUserId();
        User user = getById(userId);
        if (Objects.isNull(user)) throw new BusinessException(ErrorCodeEnum.USER_ACCOUNT_NOT_EXIST);
        user.setId(null);
        user.setPassword(null);
        return RestResp.ok(user);
    }

    public RestResp<Void> udtUserInfo(UserInfoUpdateReqDto dto) {
        Long userId = UserHolder.getUserId();
        User user = getById(userId);
        if (Objects.isNull(user)) throw new BusinessException(ErrorCodeEnum.USER_ACCOUNT_NOT_EXIST);

        if (StringUtils.hasText(dto.getUsername()) && !dto.getUsername().equals(user.getUsername())) {
            user.setUsername(dto.getUsername());
        }
        if (StringUtils.hasText(dto.getOldPassword()) && StringUtils.hasText(dto.getNewPassword())) {
            String oldPassword = DigestUtils.md5DigestAsHex(dto.getOldPassword().getBytes(StandardCharsets.UTF_8));
            if (!Objects.equals(user.getPassword(), oldPassword)) throw new BusinessException(ErrorCodeEnum.USER_PASSWORD_ERROR);
            String newPassword = DigestUtils.md5DigestAsHex(dto.getNewPassword().getBytes(StandardCharsets.UTF_8));
            user.setPassword(newPassword);
        }
        boolean success = updateById(user);
        if (!success) throw new BusinessException(ErrorCodeEnum.USER_REQ_EXCEPTION);
        return RestResp.ok();
    }

    // 获取所有用户（仅管理员）
    public RestResp<List<User>> getAllUsers() {
        if (!isAdmin(UserHolder.getUserId())) throw new BusinessException(ErrorCodeEnum.USER_UN_AUTH);
        List<User> users = list();
        users.forEach(user -> user.setPassword(null)); // 不返回密码
        return RestResp.ok(users);
    }

    // 删除用户（仅管理员）
    public RestResp<Void> deleteUser(Long userId) {
        if (!isAdmin(UserHolder.getUserId())) throw new BusinessException(ErrorCodeEnum.USER_UN_AUTH);
        removeById(userId);
        return RestResp.ok();
    }

    // 禁用启用用户（仅管理员）
    public RestResp<Void> udtUserStatus(Long userId, Integer status) {
        if (!isAdmin(UserHolder.getUserId())) throw new BusinessException(ErrorCodeEnum.USER_UN_AUTH);
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, userId).set(User::getStatus, status);
        update(updateWrapper);
        return RestResp.ok();
    }

    // 判断是否为管理员
    private boolean isAdmin(Long userId) {
        User user = getById(userId);
        return user != null && "ADMIN".equals(user.getRole());
    }
}
