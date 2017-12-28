package com.mdvns.mdvn.issue.domain;

import org.springframework.stereotype.Component;

@Component
public class RtrvIssueDetailRequest {

    private String projId;

    private String subjectId;//第一次查只返回第一个

    private String issueId;

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

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}
