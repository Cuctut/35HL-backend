package com.cuctut.hl.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserInfoUpdateReqDto {

    @NotBlank(message="用户名称不能为空！")
    @Length(min = 2,max = 10)
    private String username;

    private String oldPassword;
    private String newPassword;
}
