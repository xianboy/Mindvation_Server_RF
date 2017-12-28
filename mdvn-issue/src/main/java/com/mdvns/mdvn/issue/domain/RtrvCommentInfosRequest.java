package com.mdvns.mdvn.issue.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class RtrvCommentInfosRequest {
    private Long projId;
    private String subjectId;
}
