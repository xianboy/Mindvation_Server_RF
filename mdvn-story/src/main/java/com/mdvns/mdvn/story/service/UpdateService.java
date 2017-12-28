package com.mdvns.mdvn.story.service;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.UpdateBasicInfoRequest;
import com.mdvns.mdvn.common.bean.UpdateOtherInfoRequest;
import com.mdvns.mdvn.common.bean.UpdateStatusRequest;
import com.mdvns.mdvn.common.exception.BusinessException;

public interface UpdateService {
    //修改状态
    RestResponse<?> updateStatus(UpdateStatusRequest updateStatusRequest);
    //修改基础信息
    RestResponse<?> updateBasicInfo(UpdateBasicInfoRequest updateRequest);
    //修改其他信息
    RestResponse<?> updateOtherInfo(UpdateOtherInfoRequest updateRequest) throws BusinessException;
}
