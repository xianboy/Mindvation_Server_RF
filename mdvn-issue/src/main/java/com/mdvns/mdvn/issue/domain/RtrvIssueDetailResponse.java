package com.mdvns.mdvn.issue.domain;

import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class RtrvIssueDetailResponse {

    private IssueDetail issueDetail;
    private List<IssueIdAndIsResolved> totalElements;

    public List<IssueIdAndIsResolved> getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(List<IssueIdAndIsResolved> totalElements) {
        this.totalElements = totalElements;
    }

    public IssueDetail getIssueDetail() {
        return issueDetail;
    }

    public void setIssueDetail(IssueDetail issueDetail) {
        this.issueDetail = issueDetail;
    }
}
