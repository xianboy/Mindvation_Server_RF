package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Story implements Serializable {

    /*id pk*/
    private Long id;

    /* staff id of creator */
    private Long creatorId;

    /*story所在需求的模板id*/
//    private Long templateId;

    /*项目编号*/
    private String projSerialNo;

    /*编号(serialNo)：Pxx-Rxx-Sxx */
    private String serialNo;

    /*概要(summary)*/
    private String summary;

    /*优先级(priority)*/
    private Integer priority;

    /* start date of this requirement*/
    private Long startDate;

    /* end date of this requirement*/
    private Long endDate;

    /*用户故事点(story point)*/
    private Double storyPoint;

    /*(进度)progress*/
    private Double progress;

    /*成员数量*/
    private Integer memberAmount;
}
