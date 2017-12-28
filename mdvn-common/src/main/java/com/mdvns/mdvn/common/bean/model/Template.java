package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Template {

    private Long id;

    /*创建人ID*/
    private Long creatorId;

    /*名称*/
    private String name;

    /*编号*/
    private String serialNo;

    /*引用次数*/
    private Integer quoteCnt;

    //模板行业类型
    private Long industryId;

    /*创建时间*/
    private Long createTime;

    //1~7的随机数
    private Integer style;

    /*过程方法模块*/
    private List<TerseInfo> functionLabels;

    /*模型角色*/
    private List<TerseInfo> roles;

}
