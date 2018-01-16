package com.mdvns.mdvn.staff.domain;

import com.mdvns.mdvn.common.bean.model.AddOrRemoveById;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class UpdateStaffRequest {
    /*当前用户id*/
    @NotNull(message = "用户Id不能为空")
    private Long staffId;
    /*Staff的id,要更改的员工id*/
    @NotNull(message = "要修改的员工Id不能为空")
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
//    /*标签*/
//    private String tags;
    /*状态:active, inactive, unregistered*/
    private String status;
    /*新增或删除标签*/
    private AddOrRemoveById tags;


}
