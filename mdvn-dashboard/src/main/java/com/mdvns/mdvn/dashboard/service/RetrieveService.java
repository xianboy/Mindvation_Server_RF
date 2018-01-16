package com.mdvns.mdvn.dashboard.service;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.exception.BusinessException;

public interface RetrieveService {

    //获取指定serialNo的项目的 MVP Dashboard
    RestResponse<?> retrieveMvpDashboard(SingleCriterionRequest retrieveRequest) throws BusinessException;

}
