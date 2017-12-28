package com.mdvns.mdvn.staff.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@NoArgsConstructor
public class CreateStaffRequest {
    /*添加Staff的人*/
    @NotNull(message = "添加人id不能为空")
    @Min(value = 1, message = "id不能小于1")
    private Long creatorId;
    /*登录名*/
    @NotBlank(message = "账号不能为空")
    private String account;
    /*姓名*/
    @NotBlank(message = "用户名不能为空")
    private String name;
    /*密码*/
    @NotBlank(message = "密码不能为空")
    private String password;
    /*头像*/
    private String avatar;
    /*部门id*/
    private Long deptId;
    /*职位id*/
    private Long positionId;
    /*入职时间*/
    private Long hiredate;
    /*性别*/
    private String gender;
    /*职位级别*/
    private String positionLvl;
    /*邮箱*/
    private String email;
    /*手机号码*/
    private String mobile;
    /*标签*/
    private List<Long> tags;

}
