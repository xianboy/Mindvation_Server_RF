package com.mdvns.mdvn.mdvncomment.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreateCommentInfoRequest {

    private String replyId;
    private String projId;
    private String subjectId;
    private String creatorId;
    private String content;
    //被@的人（可有可无）
    private List<String> passiveAts;

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

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

    public List<String> getPassiveAts() {
        return passiveAts;
    }

    public void setPassiveAts(List<String> passiveAts) {
        this.passiveAts = passiveAts;
    }
}
