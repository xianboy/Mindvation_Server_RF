package com.mdvns.mdvn.mdvncomment.domain;


import com.mdvns.mdvn.mdvncomment.domain.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentDetail {
    private Comment comment;
    private Comment replyDetail;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Comment getReplyDetail() {
        return replyDetail;
    }

    public void setReplyDetail(Comment replyDetail) {
        this.replyDetail = replyDetail;
    }
}
