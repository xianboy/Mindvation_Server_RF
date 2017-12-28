package com.mdvns.mdvn.template.service;

import com.mdvns.mdvn.common.bean.*;
import com.mdvns.mdvn.common.exception.BusinessException;

public interface RetrieveService {
    //根据industryId查询模板
    RestResponse<?> retrieveByIndustryId(SingleCriterionRequest singleCriterionRequest);

    //分页查询所有数据
    RestResponse<?> retrieveAll(PageableQueryWithoutArgRequest pageableQueryWithoutArgRequest);

    //根据id集合查询基本信息
    RestResponse<?> retrieveTerseInfo(RetrieveTerseInfoRequest retrieveTerseInfoRequest);

    //根据id集合查询templateRole的基本信息
    RestResponse<?> retrieveRoleBaseInfo(RetrieveTerseInfoRequest retrieveTerseInfoRequest) throws BusinessException;

    //根据id集合查询FunctionLabel的基本信息
    RestResponse<?> retrieveLabel(RetrieveTerseInfoRequest retrieveTerseInfoRequest) throws BusinessException;

    //根据name和hostSerialNo查询过程方法
    RestResponse<?> retrieveByNameAndHost(RetrieveByNameAndHostRequest retrieveRequest);

    //根据id获取模板信息
    RestResponse<?> retrieveById(SingleCriterionRequest singleCriterionRequest) throws BusinessException;
}
