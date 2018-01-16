package com.mdvns.mdvn.department.service;

import com.mdvns.mdvn.common.bean.PageableQueryWithoutArgRequest;
import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.exception.BusinessException;

public interface RetrieveService {
    //查询所有数据：支持分页
    RestResponse<?> retrieveAll(PageableQueryWithoutArgRequest pageableQueryWithoutArgRequest) throws BusinessException;

    //获取指定id部门的详情
    RestResponse<?> retrieveDetailById(SingleCriterionRequest retrieveDetailRequest) throws BusinessException;

    //获取指定name部门的详情
    RestResponse<?> retrieveDetailByName(SingleCriterionRequest retrieveDetailRequest) throws BusinessException;
}
