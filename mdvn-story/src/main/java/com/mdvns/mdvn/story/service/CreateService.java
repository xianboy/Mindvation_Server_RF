package com.mdvns.mdvn.story.service;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.story.domain.CreateStoryRequest;

public interface CreateService {
    //创建story
    RestResponse<?> create(CreateStoryRequest createRequest) throws BusinessException;
}
