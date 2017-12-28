package com.mdvns.mdvn.requirement.domain.entity;

import com.mdvns.mdvn.common.constant.MdvnConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
public class RequirementTag {

    @Id
    @GeneratedValue
    private Long id;

    /*数据创建人Id*/
    private Long creatorId;

    /*requirementId*/
    private Long reqmntId;

    /*标签Id*/
    private Long tagId;

    /*创建时间*/
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    /*是否已删除*/
    private Integer isDeleted = MdvnConstant.ZERO;

}
