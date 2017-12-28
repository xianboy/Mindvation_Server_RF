package com.mdvns.mdvn.issue.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreateIssueAnswerRequest {

    private String projId;
    private String issueId;
    private String creatorId;
    private String content;


    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
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
}
