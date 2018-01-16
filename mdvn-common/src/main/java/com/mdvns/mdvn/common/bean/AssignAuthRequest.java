package com.mdvns.mdvn.common.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AssignAuthRequest {

//    private String projId;
    private Long projId;

//    private String assignerId;
    private Long assignerId;

//    private List<String> assignees;
    private List<Long> addList;


//    private String hierarchyId;
    private Long hostId;

    private Integer authCode;

}
