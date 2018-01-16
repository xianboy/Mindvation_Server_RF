package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class MvpDashboard implements Serializable {

    /*模板名称*/
    private String templateName;

    private StoryDashboard storyDashboard;

    private ReqmtDashboard reqmtDashboard;

}
