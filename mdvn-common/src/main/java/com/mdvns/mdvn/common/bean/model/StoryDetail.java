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

    /*过程方法*/
    private TerseInfo label;

    /*成员*/
    private List<RoleMember> members;

    /*开始结束时间*/
    private Long startDate, endDate;

    /*story point*/
    private Double storyPoint;

    /*附件*/
}
