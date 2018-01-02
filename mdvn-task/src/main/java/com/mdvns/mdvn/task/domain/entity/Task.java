package com.mdvns.mdvn.task.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdvns.mdvn.common.bean.model.TerseInfo;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"serialNo"})})
public class Task implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    /*创建人id*/
    @Column(nullable = false)
    private Long creatorId;

    /*被分配人id*/
    private Long assigneeId;

    /*编号*/
    @Column(nullable = false)
    private String serialNo;

    /*关联主体编号*/
    @Column(nullable = false)
    private String hostSerialNo;

    /*描述*/
    @Column(nullable = false)
    private String description;

    /*开始、结束日期*/
    @Column(nullable = false)
    private Timestamp startDate,endDate;

    /*进度*/
    @Column(nullable = false)
    private Integer progress = MdvnConstant.ZERO;

    /*状态*/
    @Column(nullable = false)
    private String status = MdvnConstant.NEW;

    /*备注*/
    private String comment;

    /*创建时间*/
    @Column(nullable = false)
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    /*任务交付件id*/
    @Column(nullable = false)
    private Long deliveryId;

    /*是否已删除*/
    @Column(nullable = false)
    @JsonIgnore
    private Integer isDeleted = MdvnConstant.ZERO;

    @Transient//非持久化字段
    private TerseInfo creator;

    @Transient//非持久化字段
    private Delivery delivery;

    public void setStartDate(Long startDate) {
        this.startDate = new Timestamp(startDate);
    }

    public void setEndDate(Long endDate) {
        this.endDate = new Timestamp(endDate);
    }
}
