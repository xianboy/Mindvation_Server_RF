package com.mdvns.mdvn.template.domain.entity;

import com.mdvns.mdvn.common.constant.MdvnConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
public class MvpTemplate {

    @Id
    @GeneratedValue
    private Long id;

    /*创建人id*/
    @Column(nullable = false)
    private Long creatorId;

    /*顺序*/
    @Column(nullable = false)
    private Integer mvpIndex;

    /*上层模块(模板/项目)编号*/
    @Column(nullable = false)
    private String hostSerialNo;

    /*模板id*/
    @NotNull(message = "templateId 不能为空")
    private Long templateId;

    /*状态*/
    @Column(nullable = false)
    private String status = MdvnConstant.NEW;

    /*标签创建时间*/
    @Column(nullable = false)
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    /*开始时间*/
    @Column(nullable = false)
    private Timestamp startTime = new Timestamp(System.currentTimeMillis());

    /*开始时间*/
    @Column(nullable = false)
    private Timestamp endTime = new Timestamp(System.currentTimeMillis());


    /*后加的字段，1~7随机给一个数字*/
    private Integer style = MdvnConstant.ONE;

    private Integer isDeleted = MdvnConstant.ZERO;

}
