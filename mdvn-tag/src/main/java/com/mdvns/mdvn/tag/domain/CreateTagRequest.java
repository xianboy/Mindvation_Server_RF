package com.mdvns.mdvn.tag.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CreateTagRequest implements Serializable {
    /*创建人ID*/
    private Long creatorId;
    /*标签名称*/
    private String name;
    /*引用次数*/
    private  Integer quoteCnt;
    /*色值*/
    private String color;
    /*创建时间*/
    private Long createTime;
}
