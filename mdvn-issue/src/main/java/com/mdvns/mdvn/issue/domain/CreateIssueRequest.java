package com.mdvns.mdvn.issue.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@NoArgsConstructor
public class CreateIssueRequest {

    private String subjectId;
    private Long creatorId;
    private String content;
    private Integer reward;//赏金
    private Long tagId;
    private Long projId;
}
