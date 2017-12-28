package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
public class RequirementModel implements Serializable {

    /*id pk*/
    private Long id;

    /*编号：Rxx */
    private String serialNo;

    /*概要*/
    private String summary;

    /*priority*/
    private Integer priority;

    /* start date of this requirement*/
    private Long startDate;

    /* end date of this requirement*/
    private Long endDate;

    /*负责人*/
    private TerseInfo creator;

    /*成员人数*/
    private Integer memberAmount;
}
