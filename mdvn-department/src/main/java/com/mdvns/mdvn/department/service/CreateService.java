package com.mdvns.mdvn.department.service;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.department.domain.CreateDeptRequest;

public interface CreateService {

    /*新建部门*/
    RestResponse<?> create(CreateDeptRequest deptRequest) throws BusinessException;
}
