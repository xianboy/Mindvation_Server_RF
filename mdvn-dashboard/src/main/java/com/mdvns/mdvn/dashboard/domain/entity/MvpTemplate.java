package com.mdvns.mdvn.dashboard.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
public class MvpTemplate implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    /*创建人ID*/
    @Column(nullable = false)
    private Long creatorId;

    /*mvp序号*/
    @Column(nullable = false)
    private Integer mvpIndex;

    /*MVP依赖的主体(项目/模板)编号*/
    private String hostSerialNo;

    /*模板id*/
    @NotNull(message = "templateId 不能为空")
    private Long templateId;

    /*状态*/
    @Column(nullable = false)
    private String status;

    /*创建时间*/
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    /*开始时间*/
    @Column(nullable = false)
    private Timestamp startTime, endTime;

    /*是否已删除*/
    @JsonIgnore
    private Integer isDeleted = MdvnConstant.ZERO;

}
