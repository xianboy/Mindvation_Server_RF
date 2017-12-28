package com.mdvns.mdvn.issue.domain;

import org.springframework.stereotype.Component;

@Component
public class RtrvCommentInfosRequest {
    private String projId;
    private String subjectId;

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
}
