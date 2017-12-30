package com.mdvns.mdvn.project.service;

import com.mdvns.mdvn.common.bean.model.AddOrRemoveById;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.project.domain.entity.ProjectTemplate;

import java.util.List;

public interface TemplateService {
    //新建项目成功后，创建项目模板映射
    List<ProjectTemplate> createProjectTemplate( Long creatorId, Long templateId, List<Long> templates);
    //更新项目模板映射
    void updateProjectTemplate(Long staffId, Long projId, AddOrRemoveById templates) throws BusinessException;
}
