package com.mdvns.mdvn.project.service.impl;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.model.AddOrRemoveById;
import com.mdvns.mdvn.common.bean.model.AttchInfo;
import com.mdvns.mdvn.common.bean.model.BuildAttachesById;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.FileUtil;
import com.mdvns.mdvn.common.util.MdvnCommonUtil;
import com.mdvns.mdvn.common.util.MdvnStringUtil;
import com.mdvns.mdvn.common.util.RestResponseUtil;
import com.mdvns.mdvn.project.config.WebConfig;
import com.mdvns.mdvn.project.domain.CreateProjectRequest;
import com.mdvns.mdvn.project.domain.entity.Project;
import com.mdvns.mdvn.project.repository.ProjectRepository;
import com.mdvns.mdvn.project.service.CreateService;
import com.mdvns.mdvn.project.service.LeaderService;
import com.mdvns.mdvn.project.service.TagService;
import com.mdvns.mdvn.project.service.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class CreateServiceImpl implements CreateService {
    /*实例化LOG常量*/
    private static final Logger LOG = LoggerFactory.getLogger(CreateServiceImpl.class);

    /*注入project repository*/
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private LeaderService projectStaffService;

    @Autowired
    private TagService projectTagService;

    @Autowired
    private TemplateService projectTemplateService;

    /* 注入RestTemplate*/
    @Autowired
    private RestTemplate restTemplate;

    /*注入WebConfig*/
    @Autowired
    private WebConfig webConfig;

    /**
     * 新建项目
     *
     * @param createRequest createRequest
     * @return RestResponse
     */
    @Override
    @Transactional
    public RestResponse<?> create(CreateProjectRequest createRequest) throws BusinessException {
        //根据request中的name查询project
        String name = createRequest.getName();
        Project project = this.projectRepository.findByName(name);
        //根据name查询, 如果项目已存在,抛出异常
        MdvnCommonUtil.existingError(project, "name", name);
        //根据 createProjectRequest 构建project对象
        project = buildProjectByRequest(createRequest);
        //保存
        project = this.projectRepository.saveAndFlush(project);
        //项目保存成功，保存leader: ManyToMany
        if (!(null == createRequest.getLeaders() || createRequest.getLeaders().isEmpty())) {
            this.projectStaffService.createProjectStaff(createRequest.getCreatorId(), project.getId(), createRequest.getLeaders());
        }
        //项目保存成功，保存tag: ManyToMany
        if (!(null == createRequest.getTags() || createRequest.getTags().isEmpty())) {
            this.projectTagService.createProjectTag(createRequest.getCreatorId(), project.getId(), createRequest.getTags());
        }
        //项目保存成功，保存template: ManyToMany
        if (!(null == createRequest.getTemplates() || createRequest.getTemplates().isEmpty())) {
            this.projectTemplateService.createProjectTemplate(createRequest.getCreatorId(), project.getId(), createRequest.getTemplates());
        }
        return RestResponseUtil.success(project);
    }

    /**
     * 根据 request 构建project对象
     *
     * @param request request
     * @return project
     */
    private Project buildProjectByRequest(CreateProjectRequest request) throws BusinessException {
        LOG.info("创建项目,开始制定buildProjectByRequest...");
        Project project = new Project();
        //给项目编号赋值
        project.setSerialNo(buildSerialNo4Proj());
        //项目名称
        project.setName(request.getName());
        //描述
        project.setDescription(request.getDescription());
        //优先级
        if (null != request.getPriority()) {
            project.setPriority(request.getPriority());
        }
        //开始日期
        if (null != request.getStartDate()) {
            project.setStartDate(request.getStartDate());
        }
        //结束日期
        if (null != request.getEndDate()) {
            project.setEndDate(request.getEndDate());
        }
        //创建时间
        project.setCreateTime(new Timestamp(System.currentTimeMillis()));
        //是否已删除
        project.setIsDeleted(MdvnConstant.ZERO);
        //可调整系数
        if (null != request.getContingency()) {
            project.setContingency(request.getContingency());
        }
        //创建人id
        project.setCreatorId(request.getCreatorId());
        //附件
        if (!StringUtils.isEmpty(request.getAttaches())) {
//            this.buildProjAttaches(request, project);
            List<Long> attaches = request.getAttaches();
            String serialNo = project.getSerialNo();
            String aches = MdvnStringUtil.joinLong(attaches, ",");
            project.setAttaches(aches);
            FileUtil.buildAttaches(attaches,serialNo);
        }
        return project;
    }
    /**
     * 构建项目编号:
     * 1.查询表中的最大id  maxId
     * 2.serialNum = "P" + (maxId + 1)
     *
     * @return serialNo
     */
    private String buildSerialNo4Proj() {
        //查询表中的最大id  maxId
        Long maxId = this.projectRepository.getMaxId();
        //如果表中没有数据，则给maxId赋值为0
        if (maxId == null) {
            maxId = Long.valueOf(MdvnConstant.ZERO);
        }
        maxId += 1;
        return MdvnConstant.P + maxId;
    }

    /**
     * 构建项目附件
     *
     * @param request
     * @param project
     * @return
     */
//    private List<AttchInfo> buildProjAttaches(CreateProjectRequest request, Project project) throws BusinessException {
//        List<Long> attaches = request.getAttaches();
//        String aches = MdvnStringUtil.joinLong(attaches, ",");
//        project.setAttaches(aches);
//        /**
//         * 更改附件的状态
//         */
//        BuildAttachesById buildAttachesById = new BuildAttachesById();
//        AddOrRemoveById addOrRemoveById = new AddOrRemoveById();
//        addOrRemoveById.setAddList(attaches);
//        buildAttachesById.setAddOrRemoveById(addOrRemoveById);
//        buildAttachesById.setSubjectId(project.getSerialNo());
//        List<AttchInfo> attchInfos = new ArrayList<>();
//        try {
//            attchInfos = this.restTemplate.postForObject(webConfig.getUpdateAttachesUrl(), buildAttachesById, List.class);
//        } catch (Exception ex) {
//            LOG.error("创建项目添加附件信息失败");
//            throw new BusinessException(ErrorEnum.ATTACHES_CREATE_FAILD, "添加附件信息失败");
//        }
//        return attchInfos;
//    }
//    /**
//     * 构建项目附件
//     *
//     * @param request
//     * @param project
//     * @return
//     */
//    private List<AttchInfo> buildProjAttaches(CreateProjectRequest request, Project project) throws BusinessException {
//        List<Long> attaches = request.getAttaches();
//        String serialNo = project.getSerialNo();
//        String aches = MdvnStringUtil.joinLong(attaches, ",");
//        project.setAttaches(aches);
//        /**
//         * 更改附件的状态
//         */
//        List<AttchInfo> attchInfos = new ArrayList<>();
//        try {
//            attchInfos = this.restTemplate.postForObject(webConfig.getUpdateAttachesUrl(), FileUtil.buildAttaches(attaches,serialNo), List.class);
//        } catch (Exception ex) {
//            LOG.error("创建项目添加附件信息失败");
//            throw new BusinessException(ErrorEnum.ATTACHES_CREATE_FAILD, "添加附件信息失败");
//        }
//        return attchInfos;
//    }
}
