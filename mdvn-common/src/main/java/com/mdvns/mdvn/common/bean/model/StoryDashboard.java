package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class StoryDashboard implements Serializable {

    /*模板名称*/
    private String templateName;

    /*还未分配到mvpId中的story*/
    private List<Story> backlogs;

    /*当前的mvp*/
    private List<Story> currentMvp;

    /*下一个mvp*/
    private List<Story> nextMvp;

}
