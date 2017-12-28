package com.mdvns.mdvn.requirement.service;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.requirement.domain.CreateRequirementRequest;

/**
 * create interface
 */
public interface CreateService {
   /*新建requirement*/
    RestResponse<?> create(CreateRequirementRequest requirement) throws BusinessException;

}
