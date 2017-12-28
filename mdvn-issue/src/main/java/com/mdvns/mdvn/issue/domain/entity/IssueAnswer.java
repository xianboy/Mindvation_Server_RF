package com.mdvns.mdvn.issue.domain.entity;


import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Component
@Entity
public class IssueAnswer {
    @Id
    @GeneratedValue
    private Integer uuId;
    private String answerId;
    private String issueId;
    private String creatorId;
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
    private String projId;

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public Integer getUuId() {
        return uuId;
    }

    public void setUuId(Integer uuId) {
        this.uuId = uuId;
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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
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

    @Override
    public String toString() {
        return "IssueAnswer{" +
                "uuId=" + uuId +
                ", answerId='" + answerId + '\'' +
                ", issueId=" + issueId +
                ", creatorId='" + creatorId + '\'' +
                ", createTime=" + createTime +
                ", content='" + content + '\'' +
                ", isAdopt=" + isAdopt +
                ", likeQty=" + likeQty +
                ", dislikeQty=" + dislikeQty +
                ", likeIds='" + likeIds + '\'' +
                ", dislikeIds='" + dislikeIds + '\'' +
                '}';
    }
}
