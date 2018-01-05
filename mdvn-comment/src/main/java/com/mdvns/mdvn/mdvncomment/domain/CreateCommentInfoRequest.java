package com.mdvns.mdvn.mdvncomment.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@NoArgsConstructor
public class CreateCommentInfoRequest {

    //回复
    private String replyId;
    private String projId;
    //所属的需求或者story
    private String subjectId;
    private Long creatorId;
    private String content;
    //被@的人（可有可无）
    private List<Long> passiveAts;
}
