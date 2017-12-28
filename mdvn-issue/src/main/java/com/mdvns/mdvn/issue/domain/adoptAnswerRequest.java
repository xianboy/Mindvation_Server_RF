package com.mdvns.mdvn.issue.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class adoptAnswerRequest {
    private Long creatorId;
    private String answerId;
    private String issueId;
}
