package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
public class FunctionLabel implements Serializable {

    private Long id;

    /*创建人ID*/
    private Long creatorId;

    /*编号*/
    private String serialNo;

    /*上层关联对象的编号*/
    private String hostSerialNo;

    /*名称*/
    private String name;

    /*创建时间*/
    private Timestamp createTime;

    /*子模块名称*/
    private List<TerseInfo> subLabels;

}
