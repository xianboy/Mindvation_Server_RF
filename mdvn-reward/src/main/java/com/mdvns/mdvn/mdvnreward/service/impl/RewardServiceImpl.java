package com.mdvns.mdvn.mdvnreward.service.impl;

import com.mdvns.mdvn.common.bean.PageableQueryWithoutArgRequest;
import com.mdvns.mdvn.common.bean.PageableResponse;
import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.bean.model.PageableCriteria;
import com.mdvns.mdvn.common.bean.model.Staff;
import com.mdvns.mdvn.common.bean.model.Tag;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.*;
import com.mdvns.mdvn.mdvnreward.config.WebConfig;
import com.mdvns.mdvn.mdvnreward.domain.CreateRewardRequest;
import com.mdvns.mdvn.mdvnreward.domain.ReceiveARewardRequest;
import com.mdvns.mdvn.mdvnreward.domain.RewardInfo;
import com.mdvns.mdvn.mdvnreward.domain.RtrvRewardDetailRequest;
import com.mdvns.mdvn.mdvnreward.domain.entity.Reward;
import com.mdvns.mdvn.mdvnreward.repository.RewardRepository;
import com.mdvns.mdvn.mdvnreward.service.RewardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:
 * @Description: Reward sapi业务处理
 * @Date:
 */
@Service
public class RewardServiceImpl implements RewardService {

    /* 日志常亮 */
    private static final Logger LOG = LoggerFactory.getLogger(RewardServiceImpl.class);

    private final String CLASS = this.getClass().getName();
    /*Dashboard Repository*/
    @Autowired
    private RewardRepository rewardRepository;

    /*注入WebConfig*/
    @Autowired
    private WebConfig webConfig;

    /**
     * 发布求助（创建）
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public RestResponse<?> createRewardInfo(CreateRewardRequest request) throws BusinessException {
        LOG.info("开始执行{} createRewardInfo()方法.", this.CLASS);
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        //save Reward表
        Reward reward = new Reward();
        Long creatorId = request.getCreatorId();
        String content = request.getContent();
        Integer welfareScore = request.getWelfareScore();
        Integer contribution = request.getContribution();
        Long tagId = request.getTagId();
        reward.setCreateTime(currentTime);
        reward.setCreatorId(creatorId);
        reward.setContent(content);
        reward.setWelfareScore(welfareScore);
        reward.setContribution(contribution);
        reward.setTagId(tagId);
        reward = this.rewardRepository.saveAndFlush(reward);
        reward.setSerialNo("RW" + reward.getId());
        reward = this.rewardRepository.saveAndFlush(reward);

        //返回对象信息
        RewardInfo rewardInfo = this.getRewardInfo(reward);

        /**
         * 消息推送(创建reward)
         */
        this.serverPushByCreateReward(request.getCreatorId(),reward);

        LOG.info("结束执行{} createRewardInfo()方法.", this.CLASS);
        return RestResponseUtil.success(rewardInfo);
    }

    /**
     * 获取悬赏榜列表信息(所有)
     *
     * @param request
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional
    public RestResponse<?> rtrvRewardList(PageableQueryWithoutArgRequest request) throws BusinessException {
        //获取分页参数对象
        PageableCriteria pageableCriteria = request.getPageableCriteria();
        //构建PageRequest
        PageRequest pageRequest;
        if (null == pageableCriteria) {
            LOG.info("用户[{}]没有填写分页参数，故使用默认分页.", request.getStaffId());
            pageRequest = PageableQueryUtil.defaultPageReqestBuilder();
        } else {
            pageRequest = PageableQueryUtil.pageRequestBuilder(pageableCriteria);
        }
        //分页查询
        Page<Reward> rewardPage = this.rewardRepository.findAll(pageRequest);
        //返回结果
        return RestResponseUtil.success(rewardPage);
    }

    /**
     * 获取悬赏榜列表信息(未解决)
     *
     * @param request
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional
    public RestResponse<?> rtrvUnsolvedRewardList(PageableQueryWithoutArgRequest request) throws BusinessException {
        //获取分页参数对象
        PageableCriteria pageableCriteria = request.getPageableCriteria();
        //构建PageRequest
        PageRequest pageRequest;
        if (null == pageableCriteria) {
            LOG.info("用户[{}]没有填写分页参数，故使用默认分页.", request.getStaffId());
            pageRequest = PageableQueryUtil.defaultPageReqestBuilder();
        } else {
            pageRequest = PageableQueryUtil.pageRequestBuilder(pageableCriteria);
        }
        //分页查询
        Page<Reward> rewardPage = this.rewardRepository.findByIsUnveilAndIsDeleted(pageRequest,0,0);
        //返回结果
        return RestResponseUtil.success(rewardPage);
    }

    /**
     * 获取悬赏榜列表信息(已解决)
     *
     * @param request
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional
    public RestResponse<?> rtrvResolvedRewardList(PageableQueryWithoutArgRequest request) throws BusinessException {
        //获取分页参数对象
        PageableCriteria pageableCriteria = request.getPageableCriteria();
        //构建PageRequest
        PageRequest pageRequest;
        if (null == pageableCriteria) {
            LOG.info("用户[{}]没有填写分页参数，故使用默认分页.", request.getStaffId());
            pageRequest = PageableQueryUtil.defaultPageReqestBuilder();
        } else {
            pageRequest = PageableQueryUtil.pageRequestBuilder(pageableCriteria);
        }
        //分页查询
        Page<Reward> rewardPage = this.rewardRepository.findByIsUnveilAndIsDeleted(pageRequest,1,0);
        //返回结果
        return RestResponseUtil.success(rewardPage);
    }

    /**
     * 获取某个悬赏信息
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public RestResponse<?> rtrvRewardDetail(RtrvRewardDetailRequest request) throws BusinessException {
        LOG.info("开始执行{} rtrvRewardDetail()方法.", this.CLASS);
        /**
         * 1.rewardInfo
         */
        String serialNo = request.getCriterion();
        Reward reward = this.rewardRepository.findBySerialNo(serialNo);
        //数据不存在，抛异常
        MdvnCommonUtil.notExistingError(reward, MdvnConstant.ID, serialNo);
        //返回对象信息
        RewardInfo rewardInfo = this.getRewardInfo(reward);
        LOG.info("结束执行{} rtrvRewardDetail()方法.", this.CLASS);
        return RestResponseUtil.success(rewardInfo);
    }

    /**
     * 揭榜（reward中isUnveil变为1）接受悬赏
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public RestResponse<?> receiveAReward(ReceiveARewardRequest request) throws BusinessException {
        LOG.info("开始执行{} receiveAReward()方法.", this.CLASS);
        Long unveilId = request.getStaffId();
        String serialNo = request.getSerialNo();
        /**
         * reward中isResolved变为1
         */
        Reward reward = this.rewardRepository.findBySerialNo(serialNo);
        if (reward.getIsUnveil() == MdvnConstant.ONE) {
            LOG.info("该悬赏已经被揭榜");
            throw new BusinessException(ErrorEnum.REWARD_HAS_UNVEILED, "该悬赏已经被揭榜");
        }
        reward.setIsUnveil(MdvnConstant.ONE);
        reward.setUnveilId(unveilId);
        this.rewardRepository.saveAndFlush(reward);
        //返回对象信息
        RewardInfo rewardInfo = this.getRewardInfo(reward);

        /**
         * 消息推送(揭榜)
         */
        this.serverPushByReceiveAReward(unveilId, reward);

        LOG.info("结束执行{} receiveAReward()方法.", this.CLASS);
        return RestResponseUtil.success(rewardInfo);
    }

    /**
     * 查询一周内热门标签数据：支持分页
     * @return restResponse
     */
    @Override
    @Transactional
    public RestResponse<?> retrieveHotTagList(PageableQueryWithoutArgRequest pageableQueryWithoutArgRequest) {
        //获取分页参数对象
        PageableCriteria pageableCriteria = pageableQueryWithoutArgRequest.getPageableCriteria();
        //构建PageRequest
        PageRequest pageRequest;
        if (null == pageableCriteria) {
            LOG.info("用户[{}]没有填写分页参数，故使用默认分页.", pageableQueryWithoutArgRequest.getStaffId());
            pageRequest = PageableQueryUtil.defaultPageReqestBuilder();
        } else {
            pageRequest = PageableQueryUtil.pageRequestBuilder(pageableCriteria);
        }
        //分页查询
        Page<Tag> deptPage = this.rewardRepository.findTagListInfo(pageRequest);
        //返回结果
        return RestResponseUtil.success(deptPage);
    }

    /**
     * 创建reward的消息推送
     *
     * @param reward
     * @throws BusinessException
     */
    private void serverPushByCreateReward(Long staffId,Reward reward) throws BusinessException {
        try {
            Long initiatorId = reward.getCreatorId();
            String serialNo = reward.getSerialNo();
            String subjectType = "reward";
            String type = "create";
            //获取所有员工的IdList
            List<Long> staffIds = this.getStaffIds(staffId);
            ServerPushUtil.serverPush(initiatorId, serialNo, subjectType, type, staffIds);
            LOG.info("创建reward，消息推送成功");
        } catch (Exception e) {
            LOG.error("消息推送(创建reward)出现异常，异常信息：" + e);
        }
    }

    /**
     * 揭榜的消息推送
     *
     * @param staffId,reward
     * @throws BusinessException
     */
    private void serverPushByReceiveAReward(Long staffId,Reward reward) throws BusinessException {
        try {
            Long initiatorId = staffId;
            String serialNo = reward.getSerialNo();
            String subjectType = "reward";
            String type = "unveil";
            //获取所有员工的IdList
            List<Long> staffIds = new ArrayList<>();
            staffIds.add(reward.getCreatorId());
            ServerPushUtil.serverPush(initiatorId, serialNo, subjectType, type, staffIds);
            LOG.info("揭榜，消息推送成功");
        } catch (Exception e) {
            LOG.error("消息推送(揭榜)出现异常，异常信息：" + e);
        }
    }

    /**
     * 通过staffId获取staff详情
     *
     * @param id
     * @return
     */
    public Staff rtrvStaffInfoById(Long id) {
        //实例化restTem对象
        RestTemplate restTemplate = new RestTemplate();
        String retrieveByIdUrl = webConfig.getRtrvStaffInfoByIdUrl();
        SingleCriterionRequest singleCriterionRequest = new SingleCriterionRequest();
        singleCriterionRequest.setCriterion(String.valueOf(id));
        singleCriterionRequest.setStaffId(id);
        ParameterizedTypeReference<RestResponse<Staff>> typeRef = new ParameterizedTypeReference<RestResponse<Staff>>() {
        };
        ResponseEntity<RestResponse<Staff>> responseEntity = restTemplate.exchange(retrieveByIdUrl, HttpMethod.POST, new HttpEntity<Object>(singleCriterionRequest), typeRef, RestResponse.class);
        RestResponse<Staff> restResponse = responseEntity.getBody();
        return restResponse.getData();
    }


    /**
     * 赋值并返回人员及标签对象信息
     *
     * @param reward
     * @return
     */
    private RewardInfo getRewardInfo(Reward reward) {
        RewardInfo rewardInfo = new RewardInfo();
        //赋值...
        BeanUtils.copyProperties(reward, rewardInfo);
        rewardInfo.setCreateTime(reward.getCreateTime().getTime());
        //查询创建者对象信息
        Staff creatorInfo = this.rtrvStaffInfoById(reward.getCreatorId());
        rewardInfo.setCreatorInfo(creatorInfo);
        if (!StringUtils.isEmpty(reward.getTagId())) {
            //实例化restTemplate对象
            RestTemplate restTemplate = new RestTemplate();
            //查询tag对象信息
            String findByIdUrl = webConfig.getFindTagInfoByIdUrl();
            findByIdUrl = StringUtils.replace(findByIdUrl, "{tagId}", String.valueOf(reward.getTagId()));
            Tag tag = restTemplate.postForObject(findByIdUrl, reward.getTagId(), Tag.class);
            rewardInfo.setTagInfo(tag);
        }
        //查询揭榜者对象信息
        if(!StringUtils.isEmpty(reward.getUnveilId())){
            Staff unveilInfo = this.rtrvStaffInfoById(reward.getUnveilId());
            rewardInfo.setUnveilInfo(unveilInfo);
        }
        return rewardInfo;
    }


    /**
     * 获取所有员工的IdList
     * @param staffId
     * @return
     * @throws BusinessException
     */
    private List<Long> getStaffIds(Long staffId) throws BusinessException {
        PageableQueryWithoutArgRequest request = new PageableQueryWithoutArgRequest();
        PageableCriteria pageableCriteria = new PageableCriteria();
        pageableCriteria.setPage(MdvnConstant.ONE);
        pageableCriteria.setSize(MdvnConstant.MAX);
        request.setPageableCriteria(pageableCriteria);
        request.setStaffId(staffId);
        //访问staff,获取所有人员
        //实例化restTem对象
        RestTemplate restTemplate = new RestTemplate();
        //构建retrieveAllStaffUrl
        String retrieveAllStaffUrl = webConfig.getRetrieveAllStaffUrl();
        //构建ParameterizedTypeReference
        ParameterizedTypeReference<RestResponse<PageableResponse<Staff>>> typeReference = new ParameterizedTypeReference<RestResponse<PageableResponse<Staff>>>() {
        };
        //构建requestEntity
        HttpEntity<?> requestEntity = new HttpEntity<>(new PageableQueryWithoutArgRequest(staffId, pageableCriteria));
        //构建responseEntity
        ResponseEntity<RestResponse<PageableResponse<Staff>>> responseEntity = restTemplate.exchange(retrieveAllStaffUrl, HttpMethod.POST, requestEntity, typeReference);
        //构建restResponse
        RestResponse<PageableResponse<Staff>> restResponse = responseEntity.getBody();
        //如果code不是“000”, 抛出异常
        if (!MdvnConstant.SUCCESS_CODE.equals(restResponse.getCode())) {
            LOG.error("获取指定项目的需求列表失败: {}", restResponse.getMsg());
            throw new BusinessException(restResponse.getCode(), restResponse.getMsg());
        }
        //获取staffIdList
        List<Staff> staffList = restResponse.getData().getContent();
        List<Long> staffIdList = new ArrayList<>();
        for (int i = 0; i < staffList.size(); i++) {
            Long sId = staffList.get(i).getId();
            staffIdList.add(sId);
        }
        return staffIdList;
    }

}
