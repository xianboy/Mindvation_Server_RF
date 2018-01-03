package com.mdvns.mdvn.task.domain.entity;

import com.mdvns.mdvn.common.constant.MdvnConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
public class TaskHistory {
    @Id
    @GeneratedValue
    private Long id;

    /*创建人*/
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

    private Integer deleteAttachId;

    private Integer addAttachId;

}
