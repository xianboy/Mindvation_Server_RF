package com.mdvns.mdvn.dashboard.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ArrangeDashboardRequest implements Serializable {

    private Long staffId;

    /*项目编号*/
    private String projSerialNo;

}
