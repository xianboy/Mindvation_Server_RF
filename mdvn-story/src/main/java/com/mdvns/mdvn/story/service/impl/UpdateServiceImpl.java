package com.mdvns.mdvn.story.service.impl;

import com.mdvns.mdvn.common.bean.*;
import com.mdvns.mdvn.common.bean.model.MvpContent;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.*;
import com.mdvns.mdvn.story.config.WebConfig;
import com.mdvns.mdvn.story.domain.entity.Story;
import com.mdvns.mdvn.story.repository.StoryRepository;
import com.mdvns.mdvn.story.service.MemberService;
import com.mdvns.mdvn.story.service.TagService;
import com.mdvns.mdvn.story.service.UpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 更新
 */
@Service
@Transactional
public class UpdateServiceImpl implements UpdateService {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateServiceImpl.class);

    @Resource
    private StoryRepository repository;

    @Resource
    private TagService tagService;

    @Resource
    private MemberService memberService;

    @Resource
    private WebConfig webConfig;


    /**
     * 更新状态
     * @param updateStatusRequest request
     * @return restResponse
     */
    @Override
    @Modifying
    public RestResponse<?> updateStatus(UpdateStatusRequest updateStatusRequest) {
        LOG.info("修改状态开始...");
        //更新项目状态
        this.repository.updateStatus(updateStatusRequest.getStatus(), updateStatusRequest.getHostId());
        LOG.info("修改状态成功...");
        //构建response并返回
        return RestResponseUtil.success(MdvnConstant.SUCCESS_VALUE);
    }

    /**
     * 修改基础信息
     * @param updateBasicInfoRequest request
     * @return restResponse
     */
    @Override
    @Modifying
    public RestResponse<?> updateBasicInfo(UpdateBasicInfoRequest updateBasicInfoRequest) throws BusinessException {
        LOG.info("修改基础信息开始...");
        //获取更新对象的id
        Long storyId = updateBasicInfoRequest.getHostId();
        //获取summary和description
        String summary = updateBasicInfoRequest.getFirstParam();
        String description = updateBasicInfoRequest.getSecondParam();
        //如果那么为空, 则更新描述
        if (StringUtils.isEmpty(summary)) {
            this.repository.updateDescription(description, storyId);
        } else if (StringUtils.isEmpty(description)) {
            //如果描述为空,则更新name
            this.repository.updateSummary(summary, storyId);
        } else {
            //如果都不为空, 同时更新名称和描述
            this.repository.updateBoth(summary, description, storyId);
        }
        /**
         * 消息推送
         */
        //根据id查询story
        Story story = this.repository.findOne(storyId);
        this.serverPushByUpdate(updateBasicInfoRequest.getStaffId(),story);
        LOG.info("修改基础信息成功...");
        return RestResponseUtil.success(MdvnConstant.SUCCESS_VALUE);
    }

    /**
     * 修改其他信息
     * @param updateRequest request
     * @return restResponse
     */
    @Override
    public RestResponse<?> updateOtherInfo(UpdateOtherInfoRequest updateRequest) throws BusinessException {
        buildByRequest(updateRequest);
        return RestResponseUtil.success(MdvnConstant.SUCCESS_VALUE);
    }

    /**
     * 更新STORY可选信息（附件）
     *
     * @param updateRequest
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional
    public RestResponse<?> updateOptionalInfo(UpdateOptionalInfoRequest updateRequest) throws BusinessException {
        LOG.info("更新STORY附件信息开始...");
        //STORYid
        Long storyId = updateRequest.getHostId();
        //根据id查询项目
        Story story = this.repository.findOne(storyId);
        //如果requirement不存在, 抛出异常
        MdvnCommonUtil.notExistingError(story, ErrorEnum.STORY_NOT_EXISTS, "Id为【" + storyId + "】的story不存在.");
        //更新附件
        if (null != updateRequest.getAttaches()) {
            String serialNo = story.getSerialNo();
            FileUtil.updateAttaches(updateRequest,serialNo);
        }
        /**
         * 消息推送
         */
        this.serverPushByUpdate(updateRequest.getStaffId(),story);
        LOG.info("更新STORY附件信息结束...");
        return RestResponseUtil.success(MdvnConstant.SUCCESS_VALUE);
    }

    /**
     * 构建Story
     *
     * @param updateRequest request
     * @return story
     */
    private Story buildByRequest(UpdateOtherInfoRequest updateRequest) throws BusinessException {
        //需求id
        Long storyId = updateRequest.getHostId();
        //根据id查询项目
        Story story = this.repository.findOne(storyId);
        //如果requirement不存在, 抛出异常
        MdvnCommonUtil.notExistingError(story, ErrorEnum.STORY_NOT_EXISTS, "Id为【" + storyId + "】的story不存在.");
        //优先级
        if (null != updateRequest.getPriority()) {
            story.setPriority(updateRequest.getPriority());
        }
        //开始时间
        if (null != updateRequest.getStartDate()) {
            story.setStartDate(updateRequest.getStartDate());
        }
        //结束时间
        if (null != updateRequest.getEndDate()) {
            story.setEndDate(updateRequest.getEndDate());
        }
        //story point
        if (null != updateRequest.getStoryPoint()) {
            story.setStoryPoint(updateRequest.getStoryPoint());
        }
        //更新过程方法
        if (null!=updateRequest.getLabel()) {
            story.setFunctionLabelId(buildLabel(updateRequest.getStaffId(), story.getSerialNo(), updateRequest.getLabel()));
        }
        //保存
        story = this.repository.saveAndFlush(story);
        //更新标签
        if (null != updateRequest.getTags()) {
            this.tagService.updateTags(updateRequest.getStaffId(), storyId, updateRequest.getTags());
        }
        //更新RoleMember
        if (null != updateRequest.getMembers()) {
            this.memberService.updateRoleMembers(updateRequest.getStaffId(), storyId, updateRequest.getMembers());
        }
        /**
         * 消息推送
         */
        this.serverPushByUpdate(updateRequest.getStaffId(),story);
        return story;
    }

    /**
     *  构建过程方法
     * @param creatorId creatorId
     * @param hostSerialNo hostSerialNo
     * @param functionLabel functionLabel
     * @return Long
     * @throws BusinessException BusinessException
     */
    private Long buildLabel(Long creatorId, String hostSerialNo, Object functionLabel) throws BusinessException {
        return RestTemplateUtil.buildLabel(webConfig.getCustomLabelUrl(), creatorId, hostSerialNo, functionLabel);
    }

    /**
     * 更改story的消息推送
     *
     * @param initiatorId
     * @param story
     * @throws BusinessException
     */
    private void serverPushByUpdate(Long initiatorId, Story story) throws BusinessException {
        try {
            String serialNo = story.getSerialNo();
            String subjectType = "story";
            String type = "update";
            List<Long> staffIds = this.memberService.getStoryMembers(initiatorId,story);
            if (!(null == staffIds || staffIds.isEmpty())) {
                //接受者包括story的创建者
                if (!staffIds.contains(story.getCreatorId())) {
                    staffIds.add(story.getCreatorId());
                }
            }
            ServerPushUtil.serverPush(initiatorId, serialNo, subjectType, type, staffIds);
            LOG.info("更改story信息，消息推送成功");
        } catch (Exception e) {
            LOG.error("消息推送(更改story)出现异常，异常信息：" + e);
        }
    }

    /**
     * 修改某个模板的mvp Dashboard
     *
     * @param updateRequest request
     * @return RestResponse
     */
    @Override
    @Modifying
    public RestResponse<?> updateMvpDashboard(UpdateMvpDashboardRequest updateRequest) {
        LOG.info("修改mvp Dashboard开始...");
        //遍历mvpList修改story中的mvpId
        for (MvpContent mvpContent : updateRequest.getMvpList()) {
            LOG.info("修改ID为【{}】的Story的mvpId成【{}】开始...", mvpContent.getContents(), mvpContent.getMvpId());
            this.repository.updateMvpIdByIdIn(mvpContent.getMvpId(), mvpContent.getContents());
            LOG.info("成功修改ID为【{}】的Story的mvpId成【{}】...", mvpContent.getContents(), mvpContent.getMvpId());
        }
        return RestResponseUtil.success(MdvnConstant.SUCCESS_VALUE);
    }
}
