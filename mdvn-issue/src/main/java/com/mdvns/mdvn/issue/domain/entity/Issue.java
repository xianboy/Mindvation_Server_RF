package com.mdvns.mdvn.issue.domain.entity;

import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * 发布求助（悬赏榜）
 */
@Component
@Entity
public class Issue {

    @Id
    @GeneratedValue
    private Integer uuId;
    private String issueId;
    //所属需求或者story的Id
    private String subjectId;
    private String creatorId;
    //求助描述
    @Column(columnDefinition = "text",nullable = false)
    private String content;
    private Timestamp createTime;
    private Integer reward;//赏金
    private Integer isResolved;//问题是否已解决 (1代表已解决，0代表未解决)
    private String tagId;
    private String projId;

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getUuId() {
        return uuId;
    }

    public void setUuId(Integer uuId) {
        this.uuId = uuId;
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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getReward() {
        return reward;
    }

    public void setReward(Integer reward) {
        this.reward = reward;
    }

    public Integer getIsResolved() {
        return isResolved;
    }

    public void setIsResolved(Integer isResolved) {
        this.isResolved = isResolved;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "uuId=" + uuId +
                ", issueId='" + issueId + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", creatorId='" + creatorId + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", reward=" + reward +
                ", isResolved=" + isResolved +
                ", tagId='" + tagId + '\'' +
                '}';
    }

    public Issue() {
    }

}
