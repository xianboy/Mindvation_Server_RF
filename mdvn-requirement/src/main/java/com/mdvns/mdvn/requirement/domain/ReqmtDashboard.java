package com.mdvns.mdvn.requirement.domain;

import com.mdvns.mdvn.requirement.domain.entity.Requirement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class ReqmtDashboard implements Serializable {

    private List<Requirement> backlogs;

    private List<Requirement> currentMvp;

    private List<Requirement> nextMvp;

}
