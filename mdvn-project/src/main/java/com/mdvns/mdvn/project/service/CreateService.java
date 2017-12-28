package com.mdvns.mdvn.project.service;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.project.domain.CreateProjectRequest;

public interface CreateService {
    /*create project*/
    RestResponse<?> create(CreateProjectRequest createProjectRequest) throws BusinessException;


}
