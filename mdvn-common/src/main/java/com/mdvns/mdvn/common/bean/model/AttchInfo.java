package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;


@Component
@Data
@NoArgsConstructor
public class AttchInfo {

    private Long id;

    /*上传附件的人的Id*/
    private Long creatorId;

    /*上传附件原名*/
    private String originName;

    /*附件上传后的url*/
    private String url;

    /*附件主体Id：即表明附件是project的还是requirement的*/
    private String subjectId;

    /*是否已删除*/
    private Integer isDeleted;

    /*附件信息保存时间*/
    private Timestamp createTime;

    /*附件更新时间*/
    private Timestamp updateTime;

    /*文件图标url*/
    private String iconUrl;
}
