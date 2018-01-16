package com.mdvns.mdvn.requirement.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MvpDashboard {

    private String templateName;

    private ReqmtDashboard reqmtDashboard;
}
