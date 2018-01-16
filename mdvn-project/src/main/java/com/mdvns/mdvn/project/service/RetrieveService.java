package com.mdvns.mdvn.project.service;

import com.mdvns.mdvn.common.bean.PageableQueryWithoutArgRequest;
import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.exception.BusinessException;

public interface RetrieveService {
    /*获取项目列表: 支持分页*/
    RestResponse<?> retrieveAll(PageableQueryWithoutArgRequest pageableQueryWithoutArgRequest);
    /*获取指定id项目的详情*/
    RestResponse<?> retrieveDetailBySerialNo(SingleCriterionRequest retrieveDetailRequest) throws BusinessException;
}
