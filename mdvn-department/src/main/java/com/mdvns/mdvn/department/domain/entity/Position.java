package com.mdvns.mdvn.department.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
public class Position {
    @Id
    @GeneratedValue
    private Long id;

    /*职位名称*/
    private String name;

    /*部门id*/
    private Long hostId;

    /*编号*/
    private String serialNo;

    /*创建人id*/
    private Long creatorId;

    /*创建时间*/
    private Timestamp createTime;

    /*是否已删除*/
    @JsonIgnore
    private Integer isDeleted;

    /*更改时间*/
    private Timestamp updateTime;

    public Position(String name) {
        this.name = name;
    }

}
