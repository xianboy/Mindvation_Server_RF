package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MvpTemplate {

    private Long id;

    /*创建人id*/
    private Long creatorId;

    /*顺序*/
    private Integer mvpIndex;

    /*上层模块(模板/项目)编号*/
    private String hostSerialNo;

    /*模板id*/
    private Long templateId;

    /*状态*/
    private String status;

    /*标签创建时间*/
    private Long createTime;

}
