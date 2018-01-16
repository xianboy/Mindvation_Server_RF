package com.mdvns.mdvn.issue.domain;

import com.mdvns.mdvn.common.bean.model.Staff;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@Data
@NoArgsConstructor
public class IssueRanking {

    private Long noun;//名词
    private Long resolveNum;//已解决的数量
    private Long adoptNum;//被采纳的数量
    private Staff creatorInfo;
    private Float proportion;//比例
}
