package com.mdvns.mdvn.mdvnreward.service;



import com.mdvns.mdvn.common.bean.PageableQueryWithoutArgRequest;
import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.mdvnreward.domain.CreateRewardRequest;
import com.mdvns.mdvn.mdvnreward.domain.ReceiveARewardRequest;
import com.mdvns.mdvn.mdvnreward.domain.RewardTimedPushRequest;
import com.mdvns.mdvn.mdvnreward.domain.RtrvRewardDetailRequest;
import org.springframework.transaction.annotation.Transactional;

/**
 * 评论接口
 */

public interface RewardService {


    @Transactional
    RestResponse<?> createRewardInfo(CreateRewardRequest request) throws BusinessException;

    @Transactional
    RestResponse<?> rtrvRewardAll(PageableQueryWithoutArgRequest request) throws BusinessException;

    @Transactional
    RestResponse<?> rtrvRewardList(PageableQueryWithoutArgRequest request) throws BusinessException;

    @Transactional
    RestResponse<?> rtrvUnsolvedRewardList(PageableQueryWithoutArgRequest request) throws BusinessException;

    @Transactional
    RestResponse<?> rtrvResolvedRewardList(PageableQueryWithoutArgRequest request) throws BusinessException;

    @Transactional
    RestResponse<?> rtrvRewardDetail(RtrvRewardDetailRequest request) throws BusinessException;

    @Transactional
    RestResponse<?> receiveAReward(ReceiveARewardRequest request) throws BusinessException;

    RestResponse<?> rewardTimedPush(RewardTimedPushRequest request);
}
