package com.mdvns.mdvn.issue.domain;

import com.mdvns.mdvn.common.beans.Staff;
import com.mdvns.mdvn.common.beans.Tag;
import org.springframework.stereotype.Component;

@Component
public class IssueInfo{

    private String issueId;
    //所属需求或者story的Id
    private String subjectId;
    private String creatorId;
    private String projId;
    //求助描述
    private String content;
    private Long createTime;
    private Integer reward;//赏金
    private Integer isResolved;//问题是否已解决
    private String tagId;

    private Staff creatorInfo;
    private Tag tagInfo;


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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
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

    public Staff getCreatorInfo() {
        return creatorInfo;
    }

    public void setCreatorInfo(Staff creatorInfo) {
        this.creatorInfo = creatorInfo;
    }

    public Tag getTagInfo() {
        return tagInfo;
    }

    public void setTagInfo(Tag tagInfo) {
        this.tagInfo = tagInfo;
    }
}
