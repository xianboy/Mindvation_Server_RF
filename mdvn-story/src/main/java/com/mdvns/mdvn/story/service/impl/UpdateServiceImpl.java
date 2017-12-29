package com.mdvns.mdvn.story.service.impl;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.UpdateBasicInfoRequest;
import com.mdvns.mdvn.common.bean.UpdateOtherInfoRequest;
import com.mdvns.mdvn.common.bean.UpdateStatusRequest;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.MdvnCommonUtil;
import com.mdvns.mdvn.common.util.RestResponseUtil;
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
    public RestResponse<?> updateBasicInfo(UpdateBasicInfoRequest updateBasicInfoRequest) {
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
        story = this.repository.saveAndFlush(story);
        //更新标签
        if (null != updateRequest.getTags()) {
            this.tagService.updateTags(updateRequest.getStaffId(), storyId, updateRequest.getTags());
        }
        //更新RoleMember
        if (null != updateRequest.getMembers()) {
            this.memberService.updateRoleMembers(updateRequest.getStaffId(), storyId, updateRequest.getMembers());
        }
        return story;
    }
}
