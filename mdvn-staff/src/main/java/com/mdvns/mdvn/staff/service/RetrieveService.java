package com.mdvns.mdvn.staff.service;

import com.mdvns.mdvn.common.bean.PageableQueryWithoutArgRequest;
import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.RetrieveTerseInfoRequest;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.staff.domain.RetrieveStaffRequest;

public interface RetrieveService {
    //获取指定id的Staff详情
    RestResponse<?> retrieveDetailById(SingleCriterionRequest retrieveDetailRequest) throws BusinessException;

    //获取staff列表
    RestResponse<?> retrieveAll(PageableQueryWithoutArgRequest pageableQueryWithoutArgRequest);

    RestResponse<?> retrieveTerseInfo(RetrieveTerseInfoRequest retrieveTerseInfoRequest);

    //根据name查人
    RestResponse<?> retrieveByName(SingleCriterionRequest singleCriterionRequest);

    //根据标签查人
    RestResponse<?> retrieveByNameOrTags(RetrieveStaffRequest request) throws BusinessException;

}

