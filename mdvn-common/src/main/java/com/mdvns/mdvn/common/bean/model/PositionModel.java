package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class PositionModel implements Serializable {
    private Long id;
    /*职位名称*/
    private String name;
    /*创建人id*/
    private Long creatorId;
    /*创建时间*/
    private Timestamp createTime;

    public PositionModel(String name) {
        this.name = name;
    }

}
