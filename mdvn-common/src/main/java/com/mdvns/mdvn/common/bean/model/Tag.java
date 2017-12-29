package com.mdvns.mdvn.common.bean.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Tag implements Serializable {
    /*标签id*/
    private Long id;
    /*标签编号*/
    private String serialNo;
    /*标签名称*/
    private String name;
    /*标签被引用的次数*/
    private Integer quoteCnt;
    /*创建人id*/
    private Long creatorId;
    /*標簽色值*/
    private String color;
    /*標簽創建時間*/
    private Long createTime;
    /*后加的字段, 1~7随机给一个数字*/
    private Integer tagStyle;
    /*是否已删除*/
    @JsonIgnore
    private Integer isDeleted;
}
