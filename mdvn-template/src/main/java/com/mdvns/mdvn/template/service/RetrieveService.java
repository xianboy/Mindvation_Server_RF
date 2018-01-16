package com.mdvns.mdvn.template.service;

import com.mdvns.mdvn.common.bean.*;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.template.domain.entity.MvpTemplate;

import java.util.List;

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

    //获取指定id的过程方法及其子方法
    RestResponse<?> retrieveLabelDetail(SingleCriterionRequest retrieveRequest) throws BusinessException;

    //获取指定id的交付件
    RestResponse<?> retrieveDelivery(SingleCriterionRequest retrieveRequest);

    //获取指定id的模板的所有交付件
    RestResponse<?> retrieveDeliveries(SingleCriterionRequest retrieveRequest) throws BusinessException;


    //获取指定ID的模板的迭代计划
    List<MvpTemplate> retrieveMvpTemplates(SingleCriterionRequest retrieveRequest);

    //获取mvpId为指定的值的过程方法的Id
    List<Long> retrieveLabelByMvp(SingleCriterionRequest retrieveRequest);

    //获取指定ID的模板名称
    String retrieveTemplateName(SingleCriterionRequest retrieveRequest);
}
