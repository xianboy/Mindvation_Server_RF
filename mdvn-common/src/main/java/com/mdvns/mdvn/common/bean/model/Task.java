package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Task implements Serializable {

    private Long id;

    /*编号*/
    private String serialNo;

    /*关联主体编号*/
    private String hostSerialNo;

    /*描述*/
    private String description;

    /*开始、结束日期*/
    private Long startDate,endDate;

    /*进度*/
    private Integer progress;

    /*状态*/
    private String status;

    /*备注*/
    private String comment;

    /*创建时间*/
    private Long createTime;

    /*创建人信息*/
    private TerseInfo creator;

    /*交付件*/
    private Delivery delivery;
}
