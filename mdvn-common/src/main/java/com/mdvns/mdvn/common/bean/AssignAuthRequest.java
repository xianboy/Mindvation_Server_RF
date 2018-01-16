package com.mdvns.mdvn.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignAuthRequest {
    private String projSerialNo;
    private Long assignerId;
    private List<Long> addList;
    private String hostSerialNo;
    private Integer authCode;



}
