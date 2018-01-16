package com.mdvns.mdvn.requirement.service;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.exception.BusinessException;

import java.util.List;

public interface RetrieveService {

    /**
     * 查询指定project下的requirement列表:支持分页
     * @param singleArgRequest
     * @return
     */
    RestResponse<?> retrieveListByHostSerialNo(SingleCriterionRequest singleArgRequest);

    /*根据id获取详情*/
    RestResponse<?> retrieveDetailBySerialNo(SingleCriterionRequest singleCriterionRequest) throws BusinessException;

    //获取指定编号的需求的成员
    RestResponse<?> retrieveMember(SingleCriterionRequest retrieveRequest) throws BusinessException;

    //获取指定编号的需求的过程方法id
    Long retrieveLabelIdBySerialNo(SingleCriterionRequest retrieveRequest);

    List<Long> retrieveReqMembersBySerialNo(SingleCriterionRequest singleCriterionRequest) throws BusinessException;

    RestResponse<?> retrieveReqMembersInfoBySerialNo(SingleCriterionRequest singleCriterionRequest) throws BusinessException;
}
