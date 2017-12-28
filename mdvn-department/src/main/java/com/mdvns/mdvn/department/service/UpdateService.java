package com.mdvns.mdvn.department.service;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.department.domain.UpdateDepartmentRequest;

public interface UpdateService {
    //更新部门信息
    RestResponse<?> update(UpdateDepartmentRequest updateRequest) throws BusinessException;
}
