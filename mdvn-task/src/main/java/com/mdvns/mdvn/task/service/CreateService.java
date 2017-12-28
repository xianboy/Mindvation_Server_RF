package com.mdvns.mdvn.task.service;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.task.domain.CreateTaskRequest;

public interface CreateService {
    //创建task
    RestResponse<?> create(CreateTaskRequest createRequest) throws BusinessException;

}
