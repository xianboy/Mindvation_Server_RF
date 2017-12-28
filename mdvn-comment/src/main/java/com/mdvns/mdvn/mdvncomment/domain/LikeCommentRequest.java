package com.mdvns.mdvn.mdvncomment.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class LikeCommentRequest {

    private String commentId;
    private Long creatorId;
    //like or dislike
    private String remark;
}
