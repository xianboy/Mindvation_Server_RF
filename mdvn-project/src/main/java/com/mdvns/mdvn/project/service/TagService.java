package com.mdvns.mdvn.project.service;

import com.mdvns.mdvn.common.bean.model.AddOrRemoveById;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.project.domain.entity.ProjectTag;

import java.util.List;

public interface TagService {
    //创建项目标签映射
    List<ProjectTag> createProjectTag(Long creatorId, Long projId, List<Long> tags);
    //删除标签映射
    Integer updateIsDeleted(Long staffId, Long projId, List<Long> tags, Integer isDeleted);

    //根据projId和tagId查询
    ProjectTag findByProjIdAndTagId(Long staffId, Long projId, Long tagId);
    //更新项目标签映射
    void updateTag(Long staffId, Long projId, AddOrRemoveById tags) throws BusinessException;
}
