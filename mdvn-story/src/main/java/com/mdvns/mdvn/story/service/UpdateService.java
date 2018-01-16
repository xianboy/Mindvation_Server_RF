package com.mdvns.mdvn.story.service;

import com.mdvns.mdvn.common.bean.*;
import com.mdvns.mdvn.common.exception.BusinessException;
import org.springframework.transaction.annotation.Transactional;

public interface UpdateService {
    //修改状态
    RestResponse<?> updateStatus(UpdateStatusRequest updateStatusRequest);
    //修改基础信息
    RestResponse<?> updateBasicInfo(UpdateBasicInfoRequest updateRequest) throws BusinessException;
    //修改其他信息
    RestResponse<?> updateOtherInfo(UpdateOtherInfoRequest updateRequest) throws BusinessException;

    @Transactional
    RestResponse<?> updateOptionalInfo(UpdateOptionalInfoRequest updateRequest) throws BusinessException;

    //修改某个模板的mvp Dashboard
    RestResponse<?> updateMvpDashboard(UpdateMvpDashboardRequest updateRequest);
}
