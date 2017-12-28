package com.mdvns.mdvn.mdvncomment.domain;

import org.springframework.stereotype.Component;

@Component
public class LikeCommentRequest {

    private String commentId;
    private String creatorId;
    //like or dislike
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
}
