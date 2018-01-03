package com.mdvns.mdvn.task.service;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.task.domain.UpdateAttachRequest;
import com.mdvns.mdvn.task.domain.UpdateProgressRequest;

public interface UpdateService {
    /*更新进度*/
    RestResponse<?> updateProgress(UpdateProgressRequest updateRequest) throws BusinessException;

    RestResponse<?> addAttachForTask(UpdateAttachRequest request) throws BusinessException;

    RestResponse<?> deleteAttachForTask(UpdateAttachRequest request) throws BusinessException;
}
