package com.mdvns.mdvn.mdvnreward.web;


import com.mdvns.mdvn.common.bean.PageableQueryWithoutArgRequest;
import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.mdvnreward.domain.CreateRewardRequest;
import com.mdvns.mdvn.mdvnreward.domain.ReceiveARewardRequest;
import com.mdvns.mdvn.mdvnreward.domain.RtrvRewardDetailRequest;
import com.mdvns.mdvn.mdvnreward.service.RewardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * reward控制层
 */
@CrossOrigin
@RestController
@RequestMapping(value = {"/rewards", "/v1.0/rewards"})
public class RewardController {

    private Logger LOG = LoggerFactory.getLogger(RewardController.class);
    private final String CLASS = this.getClass().getName();

    /*评论service注入*/
    @Autowired
    private RewardService rewardService;


    /**
     * 创建悬赏榜
     * @param request
     * @return
     */
    @PostMapping(value = "/createRewardInfo")
    public RestResponse<?> createRewardInfo(@RequestBody CreateRewardRequest request) throws BusinessException {
        LOG.info("开始执行{} createRewardInfo()方法.", this.CLASS);
        return this.rewardService.createRewardInfo(request);
    }

    /**
     * 获取单个悬赏榜信息
     * @param request
     * @return
     */
    @PostMapping(value = "/rtrvRewardDetail")
    public RestResponse<?> rtrvRewardDetail(@RequestBody RtrvRewardDetailRequest request) throws BusinessException {
        LOG.info("开始执行{} rtrvRewardDetail()方法.", this.CLASS);
        return this.rewardService.rtrvRewardDetail(request);
    }

    /**
     * 获取悬赏榜信息列表
     * @param request
     * @return
     */
    @PostMapping(value = "/rtrvRewardList")
    public RestResponse<?> rtrvRewardList(@RequestBody PageableQueryWithoutArgRequest request) throws BusinessException {
        LOG.info("开始执行{} rtrvRewardList()方法.", this.CLASS);
        return this.rewardService.rtrvRewardList(request);
    }

    /**
     * 获取悬赏榜信息列表(未解决)
     * @param request
     * @return
     */
    @PostMapping(value = "/rtrvUnsolvedRewardList")
    public RestResponse<?> rtrvUnsolvedRewardList(@RequestBody PageableQueryWithoutArgRequest request) throws BusinessException {
        LOG.info("开始执行{} rtrvUnsolvedRewardList()方法.", this.CLASS);
        return this.rewardService.rtrvUnsolvedRewardList(request);
    }

    /**
     * 获取悬赏榜信息列表（已解决）
     * @param request
     * @return
     */
    @PostMapping(value = "/rtrvResolvedRewardList")
    public RestResponse<?> rtrvResolvedRewardList(@RequestBody PageableQueryWithoutArgRequest request) throws BusinessException {
        LOG.info("开始执行{} rtrvResolvedRewardList()方法.", this.CLASS);
        return this.rewardService.rtrvResolvedRewardList(request);
    }

    /**
     * 揭榜
     */
    @PostMapping(value = "/receiveAReward")
    public RestResponse<?> receiveAReward(@RequestBody ReceiveARewardRequest request) throws BusinessException {
        LOG.info("开始执行{} receiveAReward()方法.", this.CLASS);
        return this.rewardService.receiveAReward(request);
    }

    /**
     * 查询一周内热门标签数据：支持分页
     * @param request
     * @return
     */
    @PostMapping(value = "/retrieveHotTagList")
    public RestResponse<?> retrieveHotTagList(@RequestBody PageableQueryWithoutArgRequest request) throws BusinessException {
        LOG.info("开始执行{} retrieveHotTagList()方法.", this.CLASS);
        return this.rewardService.retrieveHotTagList(request);
    }



}
