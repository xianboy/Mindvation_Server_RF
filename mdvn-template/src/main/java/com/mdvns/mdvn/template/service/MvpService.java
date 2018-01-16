package com.mdvns.mdvn.template.service;

import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.template.domain.CreateMvpTemplateRequest;
import com.mdvns.mdvn.template.domain.entity.MvpTemplate;

import java.util.List;

public interface MvpService {
    //创建
    MvpTemplate create(Long creatorId, Long templateId, String hostSerialNo, CreateMvpTemplateRequest createRequest);

    //获取指定serialNo的模板的迭代计划
    List<MvpTemplate> retrieveMvpTemplates(SingleCriterionRequest retrieveRequest);
}
