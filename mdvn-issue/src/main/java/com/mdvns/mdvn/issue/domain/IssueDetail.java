package com.mdvns.mdvn.issue.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@NoArgsConstructor
public class IssueDetail{
    private IssueInfo issueInfo;
    private List<IssueAnswerInfo> issueAnswerInfos;
}
