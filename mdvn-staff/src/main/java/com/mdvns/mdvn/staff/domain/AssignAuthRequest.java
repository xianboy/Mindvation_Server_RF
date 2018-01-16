package com.mdvns.mdvn.staff.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignAuthRequest {

    /*项目id*/
    private String projId;
    /*分配权限的staff*/
    private String assignerId;
    /*被添加权限的staff*/
    private List<String> assignees;
    /*项目模块id*/
    private String hierarchyId;
    /*权限代码*/
    private Integer authCode;

    public AssignAuthRequest(String projId, String assignerId, List<String> assignees, String hierarchyId) {
        this.projId = projId;
        this.assignerId = assignerId;
        this.assignees = assignees;
        this.hierarchyId = hierarchyId;
    }

}
