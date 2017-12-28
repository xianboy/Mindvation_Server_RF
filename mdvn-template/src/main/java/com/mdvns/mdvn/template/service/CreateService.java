package com.mdvns.mdvn.template.service;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.template.domain.CreateTemplateRequest;

public interface CreateService {
    //创建模板
    RestResponse<?> create(CreateTemplateRequest createRequest) throws BusinessException;
}
