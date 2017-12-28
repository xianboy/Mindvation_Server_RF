package com.mdvns.mdvn.issue.domain;

import org.springframework.stereotype.Component;

@Component
public class IssueIdAndIsResolved {
    private String issueId;
    private Integer isResolved;

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public Integer getIsResolved() {
        return isResolved;
    }

    public void setIsResolved(Integer isResolved) {
        this.isResolved = isResolved;
    }
}
