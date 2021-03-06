package com.mdvns.mdvn.requirement.service;

import com.mdvns.mdvn.common.bean.*;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.requirement.domain.ReqmtDashboard;

import java.util.List;

public interface RetrieveService {

    //查询指定project下的requirement列表:支持分页
    RestResponse<?> retrieveListByHostSerialNo(SingleCriterionRequest singleArgRequest);

    /*根据id获取详情*/
    RestResponse<?> retrieveDetailBySerialNo(SingleCriterionRequest singleCriterionRequest) throws BusinessException;

    //获取指定编号的需求的成员
    RestResponse<?> retrieveMember(SingleCriterionRequest retrieveRequest) throws BusinessException;

    RestResponse<?> retrieveReqMembersInfoBySerialNo(SingleCriterionRequest singleCriterionRequest) throws BusinessException;

    //获取指定编号的需求的过程方法id
    Long retrieveLabelIdBySerialNo(SingleCriterionRequest retrieveRequest);


    List<Long> retrieveReqMembersBySerialNo(SingleCriterionRequest singleCriterionRequest) throws BusinessException;

    //获取指定过程方法对应的需求编号
    List<String> retrieveSerialNoByLabel(RetrieveReqmtByLabelRequest retrieveRequest);

    //获取指定hostSerialNo(项目编号)和templateId的需求编号
    List<String> retrieveSerialNo(RetrieveReqmtSerialNoRequest retrieveRequest);

    //获取指定需求集合的dashboard
    ReqmtDashboard retrieveDashboard(RetrieveMvpContentRequest retrieveRequest);
}
