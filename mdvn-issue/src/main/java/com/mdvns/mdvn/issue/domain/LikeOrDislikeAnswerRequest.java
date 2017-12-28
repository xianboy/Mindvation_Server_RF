package com.mdvns.mdvn.issue.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class LikeOrDislikeAnswerRequest {

    private String answerId;
    private Long creatorId;
    //like or dislike
    private String remark;


}
