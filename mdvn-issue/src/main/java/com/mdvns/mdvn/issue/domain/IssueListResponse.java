package com.mdvns.mdvn.issue.domain;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@NoArgsConstructor
public class IssueListResponse {

    private List<IssueInfo> issueInfos;
    private Long totalElements;
}
