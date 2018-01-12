package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class StoryDetail implements Serializable{

    private Long id;

    /*编号*/
    private String serialNo;

    /*上一层模块(需求)编号*/
    private String hostSerialNo;

    /*RAG status, ie. Red, Amber, Green*/
    private String ragStatus;

    /*状态*/
    private String status;

    /*进度*/
    private Double progress;

    /*概要*/
    private String summary;

    /*描述*/
    private String description;

    /*标签*/
    private List<TerseInfo> tags;

    /*优先级*/
    private Integer priority;

    /*模板*/
    private Long templateId;

    /*过程方法*/
    private TerseInfo label;

    /*项目编号*/
    private String projSerialNo;

    /*上层过程方法及其子过程方法*/
    private FunctionLabel optionalLabel;

    /*成员*/
    private List<RoleMember> members;

    /*开始结束时间*/
    private Long startDate, endDate;

    /*story point*/
    private Double storyPoint;

    /*上层模块成员*/
    private List<RoleMember> optionalMembers;

    /*task列表*/
    private List<Task> tasks;
    /*附件列表*/
    private List<AttchInfo> attchInfos;
    /*评论列表*/
    private List<CommentDetail> commentDetails;
    /*权限信息*/
    private List<StaffAuthInfo> staffAuthInfo;;

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
