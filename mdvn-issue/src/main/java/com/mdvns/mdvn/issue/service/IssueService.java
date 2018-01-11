package com.mdvns.mdvn.issue.service;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.exception.BusinessException;
import org.springframework.transaction.annotation.Transactional;

/**
 * 评论接口
 */

public interface IssueService {

    @Transactional
    RestResponse<?> rtrvIssueList(SingleCriterionRequest request) throws BusinessException;


}
