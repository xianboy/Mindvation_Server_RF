package com.mdvns.mdvn.requirement.service.impl;

import com.mdvns.mdvn.common.bean.*;
import com.mdvns.mdvn.common.constant.AuthConstant;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.*;
import com.mdvns.mdvn.requirement.config.WebConfig;
import com.mdvns.mdvn.requirement.domain.entity.Requirement;
import com.mdvns.mdvn.requirement.repository.RequirementRepository;
import com.mdvns.mdvn.requirement.service.MemberService;
import com.mdvns.mdvn.requirement.service.TagService;
import com.mdvns.mdvn.requirement.service.UpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class UpdateServiceImpl implements UpdateService {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateServiceImpl.class);


    @Resource
    private RequirementRepository requirementRepository;

    @Resource
    private TagService tagService;

    @Resource
    private MemberService memberService;

    @Resource
    private WebConfig webConfig;

    /**
     * 更新状态
     *
     * @param updateStatusRequest request
     * @return restResponse
     */
    @Override
    @Modifying
    @Transactional
    public RestResponse<?> updateStatus(UpdateStatusRequest updateStatusRequest) {
        LOG.info("修改状态开始...");
        //更新项目状态
        this.requirementRepository.updateStatus(updateStatusRequest.getStatus(), updateStatusRequest.getHostId());
        //构建response并返回
        LOG.info("修改状态成功...");
        return RestResponseUtil.success(MdvnConstant.SUCCESS_VALUE);
    }

    /**
     * 修改基础信息
     *
     * @param updateBasicInfoRequest 更新参数对象
     * @return 更新数据条数
     */
    @Override
    @Transactional
    @Modifying
    public RestResponse<?> updateBasicInfo(UpdateBasicInfoRequest updateBasicInfoRequest) throws BusinessException {
        LOG.info("修改基础信息开始...");
        //获取更新对象的id
        Long requirementId = updateBasicInfoRequest.getHostId();
        //获取summary和desc
        String summary = updateBasicInfoRequest.getFirstParam();
        String desc = updateBasicInfoRequest.getSecondParam();
        //如果summary为空, 则更新描述
        if (StringUtils.isEmpty(summary)) {
            this.requirementRepository.updateDescription(desc, requirementId);
        } else if (StringUtils.isEmpty(desc)) {
            //如果描述为空,则更新summary
            this.requirementRepository.updateSummary(summary, requirementId);
        } else {
            //如果都不为空, 同时更新summary和描述
            this.requirementRepository.updateBoth(summary, desc, requirementId);
        }
        /**
         * 消息推送
         */
        //根据id查询项目
        Requirement requirement = this.requirementRepository.findOne(requirementId);
        this.serverPushByUpdate(updateBasicInfoRequest.getStaffId(),requirement);
        LOG.info("修改基础信息成功...");
        return RestResponseUtil.success(MdvnConstant.SUCCESS_VALUE);
    }

    /**
     * 更新其他信息
     *
     * @param updateRequest 参数对象
     * @return 更新数据条数
     */
    @Override
    @Transactional
    @Modifying
    public RestResponse<?> updateOtherInfo(UpdateOtherInfoRequest updateRequest) throws BusinessException {
        //根据request构建project对象
        buildByRequest(updateRequest);
        return RestResponseUtil.success(MdvnConstant.SUCCESS_VALUE);
    }

    /**
     * 更新需求可选信息（附件）
     *
     * @param updateRequest
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional
    public RestResponse<?> updateOptionalInfo(UpdateOptionalInfoRequest updateRequest) throws BusinessException {
        LOG.info("更新需求附件信息开始...");
        //需求id
        Long requirementId = updateRequest.getHostId();
        //根据id查询项目
        Requirement requirement = this.requirementRepository.findOne(requirementId);
        //如果requirement不存在, 抛出异常
        MdvnCommonUtil.notExistingError(requirement, ErrorEnum.REQUIREMENT_NOT_EXISTS, "Id为【" + requirementId + "】的需求不存在.");
        //更新附件
        if (null != updateRequest.getAttaches()) {
            String serialNo = requirement.getSerialNo();
            FileUtil.updateAttaches(updateRequest,serialNo);
        }
        /**
         * 消息推送
         */
        this.serverPushByUpdate(updateRequest.getStaffId(),requirement);
        LOG.info("更新需求附件信息结束...");
        return RestResponseUtil.success(MdvnConstant.SUCCESS_VALUE);
    }

    /**
     * 构建requirement
     *
     * @param updateRequest request
     * @return requirement
     */
    private Requirement buildByRequest(UpdateOtherInfoRequest updateRequest) throws BusinessException {
        //需求id
        Long requirementId = updateRequest.getHostId();
        //根据id查询项目
        Requirement requirement = this.requirementRepository.findOne(requirementId);
        //如果requirement不存在, 抛出异常
        MdvnCommonUtil.notExistingError(requirement, ErrorEnum.REQUIREMENT_NOT_EXISTS, "Id为【" + requirementId + "】的需求不存在.");
        //优先级
        if (null != updateRequest.getPriority()) {
            requirement.setPriority(updateRequest.getPriority());
        }
        //开始时间
        if (null != updateRequest.getStartDate()) {
            requirement.setStartDate(updateRequest.getStartDate());
        }
        //结束时间
        if (null != updateRequest.getEndDate()) {
            requirement.setEndDate(updateRequest.getEndDate());
        }
        requirement = this.requirementRepository.saveAndFlush(requirement);
        //更新标签
        if (null != updateRequest.getTags()) {
            this.tagService.updateTags(updateRequest.getStaffId(), requirementId, updateRequest.getTags());
        }
        //更新RoleMember
        if (null != updateRequest.getMembers()) {
            this.memberService.updateRoleMembers(updateRequest.getStaffId(), requirementId, updateRequest.getMembers());
            //更新权限
            List<Long> addList = ListUtil.getDistinctAddList(updateRequest.getMembers());
            if(!addList.isEmpty()){
                StaffAuthUtil.assignAuth(webConfig.getAssignAuthUrl(),new AssignAuthRequest(requirement.getHostSerialNo(),requirement.getCreatorId(),addList,requirement.getSerialNo(), AuthConstant.RMEMBER));
            }
            List<Long> removeList = ListUtil.getDistinctRemoveList(updateRequest.getMembers());
            if(!removeList.isEmpty()){
                StaffAuthUtil.removeAuth(webConfig.getRemoveAuthUrl(),new RemoveAuthRequest(requirement.getHostSerialNo(),requirement.getCreatorId(),removeList,requirement.getSerialNo(),AuthConstant.RMEMBER));
            }

        }
        /**
         * 消息推送
         */
        this.serverPushByUpdate(updateRequest.getStaffId(),requirement);
        return requirement;
    }

    /**
     * 更改需求的消息推送
     *
     * @param initiatorId
     * @param requirement
     * @throws BusinessException
     */
    private void serverPushByUpdate(Long initiatorId, Requirement requirement) throws BusinessException {
        try {
            String serialNo = requirement.getSerialNo();
            String subjectType = "requirement";
            String type = "update";
            List<Long> staffIds = this.memberService.getReqMembers(initiatorId,requirement);
            if (!(null == staffIds || staffIds.isEmpty())) {
                //接受者包括项目的创建者
                if (!staffIds.contains(requirement.getCreatorId())) {
                    staffIds.add(requirement.getCreatorId());
                }
            }
            ServerPushUtil.serverPush(initiatorId, serialNo, subjectType, type, staffIds);
            LOG.info("更改需求信息，消息推送成功");
        } catch (Exception e) {
            LOG.error("消息推送(更改需求)出现异常，异常信息：" + e);
        }
    }

    /**
     * 创建MVP:修改指定条件下的mvpId
     *
     * @param request request
     * @return RestResponse
     */
    @Override
    @Modifying
    public RestResponse<?> updateMvp(UpdateMvpContentRequest request) {
        this.requirementRepository.updateMvpIdBySerialNoIn(request.getMvpId(), request.getSerialNo());
        return RestResponseUtil.success(MdvnConstant.SUCCESS_VALUE);
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
        //遍历mvpList修改requirement中的mvpId
        for (MvpContent mvpContent : updateRequest.getMvpList()) {
            LOG.info("修改ID为【{}】的Requirement的mvpId成【{}】开始...", mvpContent.getContents(), mvpContent.getMvpId());
            this.requirementRepository.updateMvpIdByIdIn(mvpContent.getMvpId(), mvpContent.getContents());
            LOG.info("成功修改ID为【{}】的Requirement的mvpId成【{}】...", mvpContent.getContents(), mvpContent.getMvpId());
        }
        return RestResponseUtil.success(MdvnConstant.SUCCESS_VALUE);
    }

}
