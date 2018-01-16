package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MvpDashboard {

    private String templateName;

    private StoryDashboard storyDashboard;

    private ReqmtDashboard reqmtDashboard;
}
