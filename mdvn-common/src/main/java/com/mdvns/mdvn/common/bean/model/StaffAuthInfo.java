package com.mdvns.mdvn.common.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StaffAuthInfo {

    private Integer id;

    /*项目Id*/
    private String projId;

    /*员工Id*/
    private String staffId;

    /*权限编号*/
    private Integer authCode;

    private  String assignerId;
    
    private String hierarchyId;

}
