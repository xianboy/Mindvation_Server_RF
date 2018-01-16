package com.mdvns.mdvn.dashboard.service;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.UpdateMvpDashboardRequest;
import com.mdvns.mdvn.common.exception.BusinessException;

public interface UpdateService {
    //拖动(修改)某个模板的mvp Dashboard
    RestResponse<?> update(UpdateMvpDashboardRequest updateRequest) throws BusinessException;
}
