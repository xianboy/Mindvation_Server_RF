package com.mdvns.mdvn.issue.domain.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Component
@Entity
@Data
@NoArgsConstructor
public class IssueAnswer {
    @Id
    @GeneratedValue
    private Integer uuId;
    private String answerId;
    private String issueId;
    private Long creatorId;
    private Timestamp createTime;
    //求助回答
    @Column(columnDefinition = "text",nullable = false)
    private String content;
    private Integer isAdopt;//是否被采纳(1代表已采纳，0代表未采纳)
    //点赞这个回答的人数
    private Integer likeQty;
    //踩这个回答的人数
    private Integer dislikeQty;
    //点赞的所有人的Id
    @Column(columnDefinition = "text")
    private String likeIds;
    //踩的所有人的Id
    @Column(columnDefinition = "text")
    private String dislikeIds;
    private Long projId;

}
