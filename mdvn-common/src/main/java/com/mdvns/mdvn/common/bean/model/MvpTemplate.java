package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class MvpTemplate {

    private Long id;

    /*创建人id*/
    private Long creatorId;

    /*上层模块(模板/项目)编号*/
    private String hostSerialNo;

    /*顺序*/
    private Integer mvpIndex;

    /*模板名称*/
    private String templateName;

    /*标签创建时间*/
    private Timestamp createTime;

}
