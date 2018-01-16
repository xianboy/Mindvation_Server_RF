package com.mdvns.mdvn.issue.domain;


import com.mdvns.mdvn.common.bean.model.Staff;
import com.mdvns.mdvn.common.bean.model.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class IssueInfo{

    private String issueId;
    //所属需求或者story的Id
    private String subjectId;
    private Long creatorId;
    private String projSerialNo;//项目编号
    //求助描述
    private String content;
    private Long createTime;
    private Integer reward;//赏金
    private Integer isResolved;//问题是否已解决
    private String tagId;

    private Staff creatorInfo;
    private Tag tagInfo;
}
