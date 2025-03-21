package com.cuctut.hl.model.dto.resp;

import lombok.Builder;
import lombok.Data;

/**
 * 用户登录 响应DTO
 * @author cuctut
 * @since 2024/10/04
 */
@Data
@Builder
public class UserLoginRespDto {

    private Long uid;

    private String userName;

    private String token;

    private String role;
}
