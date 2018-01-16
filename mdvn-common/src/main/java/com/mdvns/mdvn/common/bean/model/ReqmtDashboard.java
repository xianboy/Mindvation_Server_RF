package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class ReqmtDashboard implements Serializable {

    /*还未分配到mvpId中的requirement*/
    private List<Requirement> backlogs;

    /*当前的mvp*/
    private List<Requirement> currentMvp;

    /*下一个mvp*/
    private List<Requirement> nextMvp;
}
