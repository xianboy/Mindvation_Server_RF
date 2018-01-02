package com.mdvns.mdvn.project.service;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.UpdateBasicInfoRequest;
import com.mdvns.mdvn.common.bean.UpdateStatusRequest;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.project.domain.UpdateOptionalInfoRequest;
import com.mdvns.mdvn.project.domain.UpdateOtherInfoRequest;

public interface UpdateService {
    //状态更新
    RestResponse<?> updateStatus(UpdateStatusRequest updateStatusRequest);
    //更新名称和描述
    RestResponse<?> updateBasicInfo(UpdateBasicInfoRequest updateNameAndDescRequest);
    //更新其他信息
    RestResponse<?> updateOtherInfo(UpdateOtherInfoRequest updateRequest) throws BusinessException;
    //更新可选信息
    RestResponse<?> updateOptionalInfo(UpdateOptionalInfoRequest updateRequest) throws BusinessException;
}
