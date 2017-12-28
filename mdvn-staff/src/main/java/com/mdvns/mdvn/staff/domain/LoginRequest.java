package com.mdvns.mdvn.staff.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class LoginRequest {
    /*账号*/
    @NotBlank(message = "登录名不能为空")
    private String account;
    /*密码*/
    @NotBlank(message = "密码不能为空")
    private String password;

}
