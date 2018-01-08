package com.mdvns.mdvn.common.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AssignAuthRequest {

    private String projId;

    private String assignerId;

    private List<String> assignees;

    private String hierarchyId;

    private Integer authCode;

}
