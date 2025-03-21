package com.cuctut.hl.model.dto.resp;

import lombok.Builder;
import lombok.Data;

/**
 * 用户注册 响应DTO
 * @author cuctut
 * @since 2024/10/04
 */
@Data
@Builder
public class UserRegisterRespDto {

    private Long uid;

    private String token;
}
