package com.mdvns.mdvn.task.service;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.exception.BusinessException;

public interface RetrieveService {
    //根据id获取详情
    RestResponse<?> retrieveDetailById(SingleCriterionRequest retrieveRequest) throws BusinessException;
}
