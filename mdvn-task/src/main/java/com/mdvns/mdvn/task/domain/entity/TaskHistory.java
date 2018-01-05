package com.mdvns.mdvn.task.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdvns.mdvn.common.bean.model.AttchInfo;
import com.mdvns.mdvn.common.bean.model.Staff;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
public class TaskHistory {
    @Id
    @GeneratedValue
    private Long id;

    /*创建人*/
    @JsonIgnore
    private Long creatorId;

    /*当前taskId*/
    @Column(nullable = false)
    private Long taskId;

    /*更新类目:更新进度、更新附件*/
    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private Timestamp updateTime;

    private Integer beforeProgress;

    private Integer nowProgress;

    @Column(columnDefinition = "text")
    private String beforeRemarks;

    @Column(columnDefinition = "text")
    private String nowRemarks;

    @JsonIgnore
    private Long deleteAttachId;

    @JsonIgnore
    private Long addAttachId;

    /*创建人*/
    @Transient//非持久化字段
    private Staff creator;

    /*新增附件*/
    @Transient//非持久化字段
    private AttchInfo addAttchInfo;

    /*删除的附件*/
    @Transient//非持久化字段
    private AttchInfo deleteAttchInfo;
}
