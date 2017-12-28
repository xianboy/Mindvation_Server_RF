package com.mdvns.mdvn.requirement.service;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.UpdateBasicInfoRequest;
import com.mdvns.mdvn.common.bean.UpdateOtherInfoRequest;
import com.mdvns.mdvn.common.bean.UpdateStatusRequest;
import com.mdvns.mdvn.common.exception.BusinessException;

public interface UpdateService {
    //状态更新
    RestResponse<?> updateStatus(UpdateStatusRequest updateStatusRequest);
    //跟新基础信息
    RestResponse<?> updateBasicInfo(UpdateBasicInfoRequest updateRequest);

    //修改其它信息
    RestResponse<?> updateOtherInfo(UpdateOtherInfoRequest updateRequest) throws BusinessException;
}
