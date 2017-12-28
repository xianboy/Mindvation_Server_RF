package com.mdvns.mdvn.mdvncomment.domain;

import org.springframework.stereotype.Component;


@Component
public class CreateCommentInfoResponse {

    private CommentInfo commentInfo;
    private CommentInfo replyDetail;

    public CommentInfo getCommentInfo() {
        return commentInfo;
    }

    public void setCommentInfo(CommentInfo commentInfo) {
        this.commentInfo = commentInfo;
    }

    public CommentInfo getReplyDetail() {
        return replyDetail;
    }

    public void setReplyDetail(CommentInfo replyDetail) {
        this.replyDetail = replyDetail;
    }
}
