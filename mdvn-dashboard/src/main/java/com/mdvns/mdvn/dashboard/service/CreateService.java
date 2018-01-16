package com.mdvns.mdvn.dashboard.service;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.dashboard.domain.CreateMvpRequest;

public interface CreateService {
    //新建Dashboard
    RestResponse<?> create(CreateMvpRequest createRequest);

    //根据模板自动排列mvp
    RestResponse<?> arrangeMvp(SingleCriterionRequest arrangeRequest) throws BusinessException;
}
