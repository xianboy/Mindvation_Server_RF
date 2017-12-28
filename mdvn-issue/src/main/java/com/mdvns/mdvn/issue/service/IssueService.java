package com.mdvns.mdvn.issue.service;



import com.mdvns.mdvn.common.beans.RestResponse;
import com.mdvns.mdvn.issue.domain.*;

/**
 * 评论接口
 */

public interface IssueService {

    RestResponse createIssueInfo(CreateIssueRequest request);

    RestResponse likeOrDislikeAnswer(LikeOrDislikeAnswerRequest request);

    RestResponse rtrvIssueDetail(RtrvIssueDetailRequest request);

    RestResponse createIssueAnswerInfo(CreateIssueAnswerRequest request);

    RestResponse adoptAnswer(adoptAnswerRequest request);
}
