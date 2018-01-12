package com.mdvns.mdvn.requirement.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdvns.mdvn.common.bean.model.TerseInfo;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
public class Requirement implements Serializable {

    @Id
    @GeneratedValue
    /*id pk*/
    private Long id;

    /* staff id of creator */
    @Column(nullable = false)
    private Long creatorId;

    /*所属项目id*/
    @Column(nullable = false)
    private String hostSerialNo;

    /* 需求编号：Rxx */
    @NotBlank(message = "编号不能为空")
    @Column(nullable = false)
    private String serialNo;

    /* requirement summary */
    @Column(nullable = false)
    private String summary;

    /* requirement desc*/
    @Column(columnDefinition = "text", nullable = false)
    private String description;

    /* requirement priority*/
    private Integer priority = MdvnConstant.ZERO;

    /* template ID */
    @NotNull(message = "模板不能为空")
    @Column(nullable = false)
    private Long templateId;

    /*过程方法模块*/
    @Column(nullable = false)
    private Long functionLabelId;

    /* start date of this requirement*/
    private Timestamp startDate;

    /* end date of this requirement*/
    private Timestamp endDate;

    /* create time of this requirement*/
    @Column(nullable = false)
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    /* requirement status, eg. New, Open, In progress, Closed .etc*/
    private String status = MdvnConstant.NEW;

    /* requirement RAG status, ie. Red, Amber, Green*/
    private String ragStatus = MdvnConstant.AMBER;

    /* requirement progress*/
    private Double progress = Double.valueOf(MdvnConstant.ZERO);

    /* total amount of story point and CR story point*/
    private Integer storyPointAmount = MdvnConstant.ZERO;

    /*是否被删除*/
    @JsonIgnore
    private Integer isDeleted = MdvnConstant.ZERO;

    /*最后更新时间*/
    private Timestamp lastUpdateTime;

    /*负责人*/
    @Transient//非持久化字段
    private TerseInfo creator;

    /*成员数量*/
    @Transient//非持久化字段
    private Integer memberAmount = MdvnConstant.ZERO;

    /*需求附件：id*/
    private String attaches;

    /**
     * 所属项目层级结构类型
     * possible values are : 2, 3, 4
     *
     * Explanation
     * 2 : Project -> Task
     * 3 : Project -> Requirement -> Task
     * 4 : Project -> Requirement -> Story -> Task
     */
    @Column(nullable = false)
    private Integer layerType;


    public void setStartDate(Long startDate) {
        this.startDate = new Timestamp(startDate);
    }
    public void setEndDate(Long endDate) {
        this.endDate = new Timestamp(endDate);
    }
}
