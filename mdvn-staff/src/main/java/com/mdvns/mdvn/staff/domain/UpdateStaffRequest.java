package com.mdvns.mdvn.staff.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateStaffRequest {
    /*当前用户id*/
    private Long userId;
    /*Staff的id*/
    private Long id;
    /*部门id*/
    private Long deptId;
    /*职位id*/
    private Long positionId;
    /*职位等级*/
    private String positionLvl;
    /*email*/
    private String email;
    /*手机号*/
    private String mobile;
    /*性别*/
    private String gender;
    /*标签*/
    private String tags;
    /*状态:active, inactive, unregistered*/
    private String status;


}
