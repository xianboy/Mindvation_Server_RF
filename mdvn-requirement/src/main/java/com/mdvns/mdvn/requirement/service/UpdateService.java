package com.mdvns.mdvn.requirement.service;

import com.mdvns.mdvn.common.bean.*;
import com.mdvns.mdvn.common.exception.BusinessException;
import org.springframework.transaction.annotation.Transactional;

public interface UpdateService {
    //状态更新
    RestResponse<?> updateStatus(UpdateStatusRequest updateStatusRequest);
    //跟新基础信息
    RestResponse<?> updateBasicInfo(UpdateBasicInfoRequest updateRequest) throws BusinessException;

    //修改其它信息
    RestResponse<?> updateOtherInfo(UpdateOtherInfoRequest updateRequest) throws BusinessException;

    RestResponse<?> updateOptionalInfo(UpdateOptionalInfoRequest updateRequest) throws BusinessException;

    //修改指定条件下的mvpId
    RestResponse<?> updateMvp(UpdateMvpContentRequest request);

    //修改某个模板的mvp Dashboard
    RestResponse<?> updateMvpDashboard(UpdateMvpDashboardRequest updateRequest);
}
