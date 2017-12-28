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
public class ProjectStaff {
    @Id
    @GeneratedValue
    private Long id;
    /*projId*/
    private Long projId;
    /*负责人Id*/
    private Long staffId;
    /*数据创建人id*/
    private Long creatorId;
    /*创建时间*/
    private Timestamp createTime;
    /*是否已删除*/
    private Integer isDeleted;
    /*最后更新时间*/
    private Timestamp lastUpdateTime;
}
