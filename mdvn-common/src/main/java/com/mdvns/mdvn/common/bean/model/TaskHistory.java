package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class TaskHistory {

    private Long id;

    /*创建人*/
    private Long creatorId;

    /*被修改的task*/
    private String taskId;

    /*更新类目:更新进度、更新附件*/
    private String action;

    /**/
    private Timestamp updateTime;

    private Integer beforeProgress;

    private Integer nowProgress;

    private String beforeRemarks;

    private String nowRemarks;

    private Integer deleteAttachId;

    private Integer addAttachId;

}
