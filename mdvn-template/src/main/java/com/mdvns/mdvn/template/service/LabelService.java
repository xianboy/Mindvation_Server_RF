package com.mdvns.mdvn.template.service;

import com.mdvns.mdvn.common.bean.CustomFunctionLabelRequest;
import com.mdvns.mdvn.common.bean.model.TerseInfo;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.template.domain.CreateLabelRequest;
import com.mdvns.mdvn.template.domain.entity.FunctionLabel;

import java.util.List;

public interface LabelService {

    //自定义过程方法
    Long create(CustomFunctionLabelRequest customRequest);

    //创建
    FunctionLabel create(Long creatorId, String hostSerialNo, CreateLabelRequest label) throws BusinessException;

    //获取指定id集合的FunctionLabel
    List<TerseInfo> getLabels(List<Long> ids) throws BusinessException;

    //根据name和hostSerialNo查询FunctionLabel
    List<FunctionLabel> findByHostSerialNoAndIsDeleted(String hostSerialNo, Integer zero);

    //获取指定id的模板的FunctionLabel
    List<TerseInfo> getTemplateLabel(String serialNo, Integer isDeleted) throws BusinessException;

    //获取指定id的过程方法模块及其子模块
    FunctionLabel retrieveLabelDetailById(Long id, Integer isDeleted) throws BusinessException;

    //获取指定hostSerialNo的过程方法模块及其子模块
    FunctionLabel retrieveLabelDetailByHostSerialNo(String criterion, Integer isDeleted) throws BusinessException;


//    List<TerseInfo> retrieveSubLabel(Long hostSerialNo, Integer isDeleted);
}
