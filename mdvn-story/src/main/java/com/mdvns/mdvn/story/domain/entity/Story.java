package com.mdvns.mdvn.story.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
public class Story implements Serializable {

    @Id
    @GeneratedValue
    /*id pk*/
    private Long id;

    /* staff id of creator */
    @Column(nullable = false)
    private Long creatorId;

    /*story所在需求的模板id*/
    @Column(nullable = false)
    @JsonIgnore
    private Long templateId;

    /*关联主体编号*/
    @Column(nullable = false)
    @JsonIgnore
    private String hostSerialNo;

    /*project编号*/
    @Column(nullable = false)
    private String projSerialNo;

    /*编号(serialNo)：Pxx-Rxx-Sxx */
    @NotBlank(message = "编号不能为空")
    @Column(nullable = false)
    private String serialNo;

    /*概要(summary)*/
    @Column(nullable = false)
    private String summary;

    /*描述(description)*/
    @JsonIgnore
    @Column(columnDefinition = "text", nullable = false)
    private String description;

    /*优先级(priority)*/
    private Integer priority = MdvnConstant.ZERO;

    /*过程方法模块id*/
    @Column(nullable = false)
    @JsonIgnore
    private Long functionLabelId;

    /* start date of this requirement*/
    private Timestamp startDate;

    /* end date of this requirement*/
    private Timestamp endDate;

    /* create time of this requirement*/
    @Column(nullable = false)
    @JsonIgnore
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    /*用户故事点(story point)*/
    private Double storyPoint;

    /*status, eg. New, Open, In progress, Closed .etc*/
    @JsonIgnore
    private String status = MdvnConstant.NEW;

    /*RAG status, ie. Red, Amber, Green*/
    @JsonIgnore
    private String ragStatus = MdvnConstant.AMBER;

    /*(进度)progress*/
    private Double progress = Double.valueOf(MdvnConstant.ZERO);

    /*是否被删除*/
    @JsonIgnore
    private Integer isDeleted = MdvnConstant.ZERO;

    /*成员数量*/
    @Transient//非持久化字段
    private Integer memberAmount = MdvnConstant.ZERO;

    /*STORY附件：id*/
    private String attaches;

<<<<<<< HEAD
=======
    /*Story所在mvpId*/
    private Long mvpId;

>>>>>>> parent of c74f720... Merge branch 'master' of https://github.com/xianboy/Mindvation_Server_RF

    public void setStartDate(Long startDate) {
        this.startDate = new Timestamp(startDate);
    }

    public void setEndDate(Long endDate) {
        this.endDate = new Timestamp(endDate);
    }


}
