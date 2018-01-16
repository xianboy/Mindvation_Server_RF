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
//    private String projId;
    private String projSerialNo;
    /*分配权限的staff*/
//    private String assignerId;
    private Long assignerId;
    /*被添加权限的staff*/
//    private List<String> assignees;
    private List<Long> addList;
    /*项目模块id*/
//    private String hierarchyId;
    private String hostSerialNo;
    /*权限代码*/
    private Integer authCode;

//    public AssignAuthRequest(String projId, String assignerId, List<String> assignees, String hierarchyId) {
//        this.projId = projId;
//        this.assignerId = assignerId;
//        this.assignees = assignees;
//        this.hierarchyId = hierarchyId;
//    }

}
