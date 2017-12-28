package com.mdvns.mdvn.project.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
public class ProjectTemplate {
    @Id
    @GeneratedValue
    private Long id;
    /*数据创建人Id*/
    private Long creatorId;
    /*projId*/
    private Long projId;
    /*模板Id*/
    private Long templateId;
    /*创建时间*/
    private Timestamp createTime;
    /*最后更新时间*/
    private Timestamp lastUpdateTime;
    /*是否已删除*/
    private Integer isDeleted;

}
