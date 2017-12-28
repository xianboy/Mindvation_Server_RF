package com.mdvns.mdvn.project.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.sql.Timestamp;


@Entity
@Data
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames ={"name"})})
public class Project {
    //主键
    @Id
    @GeneratedValue
    private Long id;

    /*项目创建者Id*/
    private Long creatorId;

    /*项目名称*/
    private String name;

    /*项目编号: Pxx*/
    private String serialNo;

    /*项目描述*/
    @Column(columnDefinition = "text", nullable = false)
    private String description;

    /*项目优先级*/
    @Min(value = 1, message = "优先级不能小于1")
    @Max(value = 5, message = "优先级不能大于4")
    private Integer priority = 1;

    /*项目开始日期*/
    private Timestamp startDate;

    /*项目结束时期*/
    private Timestamp endDate;

    /*创建时间*/
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    /*项目状态*/
    private String status = "new";

    /*状态色值*/
    private String ragStatus = "G";

    /*效率值*/
    private Double efficiency = 0.0;

    /*项目进度*/
    private Double progress = 0.0;

    /*项目可调整系数*/
    private Double contingency = 0.0;

    /*项目成本*/
    private Double cost = 0.0;

    /*项目需求变更成本*/
    private Double crCost = 0.0;

    /*story总数*/
    private Integer storyQty = 0;

    /*storyPoint总数*/
    private Double storyPointQty = 0.0;

    /*crStory总数*/
    private Integer crStoryQty = 0;

    /*crStoryPoint总数*/
    private Integer crStoryPointQty = 0;

    /*需求变更占比*/
    private Double crRate = 0.0;

    /*是否被删除*/
    @JsonIgnore
    private Integer isDeleted = 0;

    /*项目附件：id*/
    private String attaches;

    public void setStartDate(Long startDate) {
        this.startDate = new Timestamp(startDate);
    }

    public void setEndDate(Long endDate) {
        this.endDate = new Timestamp(endDate);
    }

}
