package com.mdvns.mdvn.story.service;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.UpdateMvpContentRequest;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.story.domain.CreateStoryRequest;

public interface CreateService {
    //创建story
    RestResponse<?> create(CreateStoryRequest createRequest) throws BusinessException;

    //创建MVP:修改指定条件下的Story的mvpId
    RestResponse<?> createMvp(UpdateMvpContentRequest request);
}
