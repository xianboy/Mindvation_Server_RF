package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Delivery implements Serializable {

    private Long id;

    /*创建人*/
    private Long creatorId;

    /*上层关联对象编号*/
    private String hostSerialNo;

    /*编号*/
    private String serialNo;

    /*名称*/
    private String name;

    /*类型(三种):1.附件和进度;2.进度;3.附件*/
    private Integer typeId;

}
