package com.mdvns.mdvn.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
