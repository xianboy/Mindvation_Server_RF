package com.mdvns.mdvn.issue.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class RtrvIssueDetailRequest {

    private Long projId;

    private String subjectId;//第一次查只返回第一个

    private String issueId;
}
