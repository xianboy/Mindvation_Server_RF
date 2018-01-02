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
import com.mdvns.mdvn.project.config.WebConfig;
import com.mdvns.mdvn.common.bean.UpdateOptionalInfoRequest;
import com.mdvns.mdvn.project.domain.UpdateOtherInfoRequest;
import com.mdvns.mdvn.project.domain.entity.Project;
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

import java.util.ArrayList;
import java.util.List;

@Service
public class UpdateServiceImpl implements UpdateService {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateServiceImpl.class);

    @Autowired
    private ProjectRepository projectRepository;


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
    public RestResponse<?> updateBasicInfo(UpdateBasicInfoRequest updateBasicInfoRequest) {
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
//            this.updateAttaches(updateRequest, project);
            FileUtil.updateAttaches(updateRequest,serialNo);
        }
        //根据projId获取详情
        //retrieveByProjId(updateRequest.getStaffId(), updateRequest.getHostId());
        LOG.info("更新项目附件信息结束...");
        return RestResponseUtil.success(MdvnConstant.SUCCESS_VALUE);
    }

//    /**
//     * 更新项目附件信息
//     *
//     * @param updateRequest
//     * @return
//     */
//    @Transactional
//    public List<AttchInfo> updateAttaches(UpdateOptionalInfoRequest updateRequest, Project project) throws BusinessException {
//        LOG.info("更新项目附件信息开始...");
//        List<Long> addList = updateRequest.getAttaches().getAddList();
//        List<Long> removeList = updateRequest.getAttaches().getRemoveList();
//        BuildAttachesById buildAttachesById = new BuildAttachesById();
//        AddOrRemoveById addOrRemoveById = new AddOrRemoveById();
//        if (null != addList && addList.size() > 0) {
//            addOrRemoveById.setAddList(addList);
//        }
//        if (null != removeList && removeList.size() > 0) {
//            addOrRemoveById.setRemoveList(removeList);
//        }
//        buildAttachesById.setAddOrRemoveById(addOrRemoveById);
//        buildAttachesById.setSubjectId(project.getSerialNo());
//        List<AttchInfo> attchInfos = new ArrayList<>();
//        try {
//            attchInfos = this.restTemplate.postForObject(webConfig.getUpdateAttachesUrl(), buildAttachesById, List.class);
//        } catch (Exception ex) {
//            LOG.error("更改指定项目的附件列表失败");
//            throw new BusinessException(ErrorEnum.ATTACHES_UPDATE_FAILD, "更改附件列表信息失败");
//        }
//        LOG.info("更新项目附件信息结束...");
//        return attchInfos;
//    }

}
