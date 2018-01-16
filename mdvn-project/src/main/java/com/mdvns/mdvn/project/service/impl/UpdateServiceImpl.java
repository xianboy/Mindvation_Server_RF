package com.mdvns.mdvn.project.service.impl;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.UpdateBasicInfoRequest;
import com.mdvns.mdvn.common.bean.UpdateStatusRequest;
import com.mdvns.mdvn.common.bean.model.AddOrRemoveById;
import com.mdvns.mdvn.common.bean.model.AttchInfo;
import com.mdvns.mdvn.common.bean.model.BuildAttachesById;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.FileUtil;
import com.mdvns.mdvn.common.util.MdvnCommonUtil;
import com.mdvns.mdvn.common.util.RestResponseUtil;
import com.mdvns.mdvn.common.util.ServerPushUtil;
import com.mdvns.mdvn.project.config.WebConfig;
import com.mdvns.mdvn.common.bean.UpdateOptionalInfoRequest;
import com.mdvns.mdvn.project.domain.UpdateOtherInfoRequest;
import com.mdvns.mdvn.project.domain.entity.Project;
import com.mdvns.mdvn.project.domain.entity.ProjectStaff;
import com.mdvns.mdvn.project.repository.LeaderRepository;
import com.mdvns.mdvn.project.repository.ProjectRepository;
import com.mdvns.mdvn.project.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UpdateServiceImpl implements UpdateService {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateServiceImpl.class);

    @Autowired
    private ProjectRepository projectRepository;

    @Resource
    private LeaderRepository projectStaffRepository;


    @Autowired
    private TagService projectTagService;

    @Autowired
    private LeaderService projectStaffService;

    @Autowired
    private TemplateService projectTemplateService;

    @Autowired
    private RetrieveService retrieveService;

    /* 注入RestTemplate*/
    @Autowired
    private RestTemplate restTemplate;

    /*注入WebConfig*/
    @Autowired
    private WebConfig webConfig;

    /**
     * 更新项目状态
     *
     * @param updateStatusRequest 更新参数对象
     * @return status
     */
    @Override
    @Modifying
    @Transactional
    public RestResponse<?> updateStatus(UpdateStatusRequest updateStatusRequest) {
        //更新项目状态
        this.projectRepository.updateStatus(updateStatusRequest.getStatus(), updateStatusRequest.getHostId());
        //构建response并返回
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
        LOG.info("更新项目基础信息开始...");
        //获取更新对象的id
        Long projId = updateBasicInfoRequest.getHostId();
        //获取name和desc
        String name = updateBasicInfoRequest.getFirstParam();
        String desc = updateBasicInfoRequest.getSecondParam();
        //如果name为空, 则更新描述
        if (StringUtils.isEmpty(name)) {
            this.projectRepository.updateDesc(desc, projId);
        } else if (StringUtils.isEmpty(desc)) {
            //如果描述为空,则更新name
            this.projectRepository.updateName(name, projId);
        } else {
            //如果都不为空, 同时更新名称和描述
            this.projectRepository.updateBasicInfo(name, desc, projId);
        }
        /**
         * 消息推送（更改项目基础信息）
         */
        Long initiatorId = updateBasicInfoRequest.getStaffId();
        //根据id查询项目
        Project project = this.projectRepository.findOne(projId);
        this.serverPushByUpdate(initiatorId,project);

        LOG.info("更新项目基础信息成功...");
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
    public RestResponse<?> updateOtherInfo(UpdateOtherInfoRequest updateRequest) throws BusinessException {

        //根据request构建project对象
        buildProjectByRequest(updateRequest);
        //根据projId获取详情
        //retrieveByProjId(updateRequest.getStaffId(), updateRequest.getHostId());
        return RestResponseUtil.success(MdvnConstant.SUCCESS_VALUE);
    }

    /**
     * 根据request构建project对象
     *
     * @param updateRequest 更新对象
     * @return project
     */
    private Project buildProjectByRequest(UpdateOtherInfoRequest updateRequest) throws BusinessException {
        LOG.info("更新项目其它信息开始...");
        //项目id
        Long projId = updateRequest.getHostId();
        //根据id查询项目
        Project project = this.projectRepository.findOne(projId);
        //如果project不存在, 抛出异常
        MdvnCommonUtil.notExistingError(project, "id", projId.toString());
        //优先级
        if (null != updateRequest.getPriority()) {
            project.setPriority(updateRequest.getPriority());
        }
        //开始时间
        if (null != updateRequest.getStartDate()) {
            project.setStartDate(updateRequest.getStartDate());
        }
        //结束时间
        if (null != updateRequest.getEndDate()) {
            project.setEndDate(updateRequest.getEndDate());
        }
        //可调整系数
        if (null != updateRequest.getContingency()) {
            project.setContingency(updateRequest.getContingency());
        }
        project = this.projectRepository.saveAndFlush(project);
        //更新标签
        if (null != updateRequest.getTags()) {
            this.projectTagService.updateTag(updateRequest.getStaffId(), projId, updateRequest.getTags());
        }
        //更新负责人
        if (null != updateRequest.getLeaders()) {
            this.projectStaffService.updateProjectLeader(updateRequest.getStaffId(), projId, updateRequest.getLeaders());
        }
        //更新模板
        if (null != updateRequest.getTemplates()) {
            this.projectTemplateService.updateProjectTemplate(updateRequest.getStaffId(), projId, updateRequest.getTemplates());
        }
        LOG.info("更新项目其它信息结束...");

        /**
         * 消息推送（更改项目其他信息）
         */
        Long initiatorId = updateRequest.getStaffId();
        this.serverPushByUpdate(initiatorId,project);

        return project;
    }

    /**
     * 更新项目可选信息（附件）
     *
     * @param updateRequest
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional
    public RestResponse<?> updateOptionalInfo(UpdateOptionalInfoRequest updateRequest) throws BusinessException {
        LOG.info("更新项目附件信息开始...");
        //项目id
        Long projId = updateRequest.getHostId();
        //根据id查询项目
        Project project = this.projectRepository.findOne(projId);
        //如果project不存在, 抛出异常
        MdvnCommonUtil.notExistingError(project, "id", projId.toString());
        //更新附件
        if (null != updateRequest.getAttaches()) {
            String serialNo = project.getSerialNo();
            FileUtil.updateAttaches(updateRequest, serialNo);
        }
        /**
         * 消息推送（更改项目可选信息）
         */
        Long initiatorId = updateRequest.getStaffId();
        this.serverPushByUpdate(initiatorId,project);

        LOG.info("更新项目附件信息结束...");
        return RestResponseUtil.success(MdvnConstant.SUCCESS_VALUE);
    }

    /**
     * 更改项目的消息推送
     *
     * @param initiatorId
     * @param project
     * @throws BusinessException
     */
    private void serverPushByUpdate(Long initiatorId, Project project) throws BusinessException {
        try {
            String serialNo = project.getSerialNo();
            String subjectType = "project";
            String type = "update";
            List<Long> staffIds = this.getStaffIds(project);
            ServerPushUtil.serverPush(initiatorId, serialNo, subjectType, type, staffIds);
            LOG.info("更改项目可选信息，消息推送成功");
        } catch (Exception e) {
            LOG.error("消息推送(更改项目)出现异常，异常信息：" + e);
        }
    }


    /**
     * 获取项目的负责人和创建者
     *
     * @param project
     * @return
     */
    private List<Long> getStaffIds(Project project) {
        //项目id
        Long projId = project.getId();
        //查询project的负责人和创建者
        List<ProjectStaff> projectStaffs = this.projectStaffRepository.findByProjIdAndIsDeleted(projId, 0);
        List<Long> staffIds = new ArrayList<>();
        for (int i = 0; i < projectStaffs.size(); i++) {
            staffIds.add(projectStaffs.get(i).getStaffId());
        }
        //查询proj的创建者
        Long createId = project.getCreatorId();
        if (!staffIds.contains(createId)) {
            staffIds.add(createId);
        }
        return staffIds;
    }

}
