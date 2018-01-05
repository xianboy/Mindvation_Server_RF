package com.mdvns.mdvn.issue.service;



import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.issue.domain.*;

/**
 * 评论接口
 */

public interface IssueService {

    RestResponse<?> createIssueInfo(CreateIssueRequest request) throws BusinessException;

    RestResponse<?> likeOrDislikeAnswer(LikeOrDislikeAnswerRequest request);

    RestResponse<?> rtrvIssueDetail(RtrvIssueDetailRequest request);

    RestResponse<?> createIssueAnswerInfo(CreateIssueAnswerRequest request) throws BusinessException;

    RestResponse<?> adoptAnswer(adoptAnswerRequest request) throws BusinessException;
}
