package com.mdvns.mdvn.template.service;

import com.mdvns.mdvn.common.bean.CustomFunctionLabelRequest;
import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.template.domain.entity.FunctionLabel;

public interface LabelService {

    //自定义过程方法
    Long create(CustomFunctionLabelRequest customRequest);

    FunctionLabel create(Long creatorId, String hostSerialNo, FunctionLabel label) throws BusinessException;
}
