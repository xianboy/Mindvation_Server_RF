package com.mdvns.mdvn.common.bean.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class CommentDetail {
    private CommentInfo commentInfo;
    private CommentInfo replyDetail;

}
