package com.mdvns.mdvn.issue.domain;


import com.mdvns.mdvn.common.bean.model.CommentDetail;
import com.mdvns.mdvn.common.bean.model.Staff;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Data
@NoArgsConstructor
public class IssueAnswerInfo {
    private String answerId;
    private String issueId;
    private Long creatorId;
    private Long createTime;
    //求助回答
    private String content;
    private Integer isAdopt;//是否被采纳
    private String projSerialNo;//项目编号
    //点赞这个回答的人数
    private Integer likeQty;
    //踩这个回答的人数
    private Integer dislikeQty;
    //点赞的所有人的Id
    private String likeIds;
    //踩的所有人的Id
    private String dislikeIds;

    private Staff creatorInfo;

    private List<CommentDetail> commentDetails;

    //点赞的所有人的Id
    private List likeIdList;
    //踩的所有人的Id
    private List dislikeIdList;
}
