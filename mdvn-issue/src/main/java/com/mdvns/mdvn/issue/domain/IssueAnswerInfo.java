package com.mdvns.mdvn.issue.domain;


import com.mdvns.mdvn.common.beans.Comment;
import com.mdvns.mdvn.common.beans.CommentDetail;
import com.mdvns.mdvn.common.beans.Staff;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IssueAnswerInfo {
    private String answerId;
    private String issueId;
    private String creatorId;
    private Long createTime;
    //求助回答
    private String content;
    private Integer isAdopt;//是否被采纳
    private String projId;
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


    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public Staff getCreatorInfo() {
        return creatorInfo;
    }

    public void setCreatorInfo(Staff creatorInfo) {
        this.creatorInfo = creatorInfo;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIsAdopt() {
        return isAdopt;
    }

    public void setIsAdopt(Integer isAdopt) {
        this.isAdopt = isAdopt;
    }

    public Integer getLikeQty() {
        return likeQty;
    }

    public void setLikeQty(Integer likeQty) {
        this.likeQty = likeQty;
    }

    public Integer getDislikeQty() {
        return dislikeQty;
    }

    public void setDislikeQty(Integer dislikeQty) {
        this.dislikeQty = dislikeQty;
    }

    public String getLikeIds() {
        return likeIds;
    }

    public void setLikeIds(String likeIds) {
        this.likeIds = likeIds;
    }

    public String getDislikeIds() {
        return dislikeIds;
    }

    public void setDislikeIds(String dislikeIds) {
        this.dislikeIds = dislikeIds;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public List<CommentDetail> getCommentDetails() {
        return commentDetails;
    }

    public void setCommentDetails(List<CommentDetail> commentDetails) {
        this.commentDetails = commentDetails;
    }
}
