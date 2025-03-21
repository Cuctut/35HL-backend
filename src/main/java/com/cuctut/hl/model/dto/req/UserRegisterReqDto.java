package com.cuctut.hl.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 用户注册 请求DTO
 *
 * @author cuctut
 * @since 2024/10/04
 */
@Data
public class UserRegisterReqDto {

    @NotBlank(message="用户名称不能为空！")
    @Length(min = 2,max = 10)
    private String username;

    @NotBlank(message="密码不能为空！")
    private String password;

}
