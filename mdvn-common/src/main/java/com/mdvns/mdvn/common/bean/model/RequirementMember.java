package com.mdvns.mdvn.common.bean.model;

import com.mdvns.mdvn.common.constant.MdvnConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequirementMember {

    private Long id;

    /*requirementId*/
    private Long requirementId;

    /*角色Id*/
    private Long roleId;

    /*成员Id*/
    private Long memberId;

    /*数据创建人id*/
    private Long creatorId;

    /*创建时间*/
    private Long createTime;

    /*是否已删除*/
    private Integer isDeleted = MdvnConstant.ZERO;

}
