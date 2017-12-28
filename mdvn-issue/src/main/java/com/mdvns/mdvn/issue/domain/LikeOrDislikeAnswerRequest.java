package com.mdvns.mdvn.issue.domain;

import org.springframework.stereotype.Component;

@Component
public class LikeOrDislikeAnswerRequest {

    private String answerId;
    private String creatorId;
    //like or dislike
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
}
