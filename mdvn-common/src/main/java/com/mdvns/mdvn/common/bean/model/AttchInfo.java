package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@NoArgsConstructor
public class AttchInfo {

    private Integer id;
    /*文件原始名称*/
    private String originName;

    /*文件上传后的url*/
    private String url;

    /*附件的主体：是project或requriement的附件*/
    private String subjectId;

    /*上传文件人的Id*/
    private Long creatorId;

    /*文件上传时间*/
    private Long createTime;

}
