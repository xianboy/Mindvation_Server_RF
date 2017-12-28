package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class Industry {

    private Long id;

    /*创建人Id*/
    private Long creatorId;

    /*名称*/
    private String name;

    /*创建时间*/
    private Timestamp createTime;

    /*引用次数*/
    private Integer quoteCnt;

    //1~7的随机数
    private Integer style;
}
