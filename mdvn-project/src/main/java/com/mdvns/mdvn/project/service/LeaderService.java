package com.mdvns.mdvn.project.service;

import com.mdvns.mdvn.common.bean.model.AddOrRemoveById;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.project.domain.entity.ProjectStaff;

import java.util.List;

public interface LeaderService {
    //新建项目时成功后，新建项目的leader映射
    List<ProjectStaff> createProjectStaff(Long creatorId, Long projId, List<Long> leaders);

    //更新项目负责人映射
    void updateProjectLeader(Long staffId, Long projId, AddOrRemoveById leaders) throws BusinessException;
}
