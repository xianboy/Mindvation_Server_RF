package com.mdvns.mdvn.requirement.domain.entity;

import com.mdvns.mdvn.common.constant.MdvnConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
public class RequirementMember {
    @Id
    @GeneratedValue
    private Long id;

    /*requirementId*/
    @Column(nullable = false)
    private Long requirementId;

    /*角色Id*/
    @Column(nullable = false)
    private Long roleId;

    /*成员Id*/
    @Column(nullable = false)
    private Long memberId;

    /*数据创建人id*/
    @Column(nullable = false)
    private Long creatorId;

    /*创建时间*/
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    /*是否已删除*/
    private Integer isDeleted = MdvnConstant.ZERO;

}
