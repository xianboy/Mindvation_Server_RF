package com.mdvns.mdvn.story.service;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.RetrieveMvpContentRequest;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.story.domain.StoryDashboard;

import java.util.List;

public interface RetrieveService {
    //获取指定id的Story的详情
    RestResponse<?> retrieveDetailById(SingleCriterionRequest singleCriterionRequest) throws BusinessException;

    //根据hostSerialNo获取story列表
    RestResponse<?> retrieveListByHostSerialNo(SingleCriterionRequest singleCriterionRequest);

    //根据hostSerialNo获取story详情
    RestResponse<?> retrieveDetailBySerialNo(SingleCriterionRequest singleCriterionRequest) throws BusinessException;

    List<Long> retrieveStoryMembersBySerialNo(SingleCriterionRequest singleCriterionRequest) throws BusinessException;

    //获取指定MvpId的Story集合
    StoryDashboard retrieveDashboard(RetrieveMvpContentRequest retrieveRequest);
}
