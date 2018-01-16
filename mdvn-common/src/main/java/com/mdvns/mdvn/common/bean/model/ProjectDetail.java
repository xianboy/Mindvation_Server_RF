package com.mdvns.mdvn.common.bean.model;

import com.mdvns.mdvn.common.bean.PageableResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class ProjectDetail implements Serializable {
    /*项目id*/
    private Long id;
    /*项目编号: Pxx*/
    private String serialNo;
    /*项目状态*/
    private String status;
    /*项目进度*/
    private Double progress;
    /*项目名称*/
    private String name;
    /*项目描述*/
    private String description;
    /*标签*/
    private List<TerseInfo> tags;
    /*项目优先级*/
    private Integer priority;
    /*负责人*/
    private List<TerseInfo> leaders;
    /*开始日期*/
    private Long startDate;
    /*结束日期*/
    private Long endDate;
    /*项目模板*/
    private List<TerseTemplate> templates;
    /*项目可调整系数*/
    private Double contingency;
    /*项目创建者对象信息*/
    private Staff creatorInfo;
    /*需求列表*/
    private PageableResponse<Requirement> requirements;
    /*附件列表*/
    private List<AttchInfo> attchInfos;
    /*权限信息*/
    private List<StaffAuthInfo> staffAuthInfo;

    /**
     * 项目层级结构类型
     * possible values are : 2, 3, 4
     *
     * Explanation
     * 2 : Project -> Task
     * 3 : Project -> Requirement -> Task
     * 4 : Project -> Requirement -> Story -> Task
     */
    private Integer layerType;


}

