package com.mdvns.mdvn.issue.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IssueDetail{
    private IssueInfo issueInfo;
    private List<IssueAnswerInfo> issueAnswerInfos;

    public IssueDetail() {
    }


    public IssueInfo getIssueInfo() {
        return issueInfo;
    }

    public void setIssueInfo(IssueInfo issueInfo) {
        this.issueInfo = issueInfo;
    }

    public List<IssueAnswerInfo> getIssueAnswerInfos() {
        return issueAnswerInfos;
    }

    public void setIssueAnswerInfos(List<IssueAnswerInfo> issueAnswerInfos) {
        this.issueAnswerInfos = issueAnswerInfos;
    }
}
