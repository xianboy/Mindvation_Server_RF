package com.mdvns.mdvn.project.service.impl;

import com.mdvns.mdvn.common.bean.PageableQueryWithoutArgRequest;
import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.RetrieveTerseInfoRequest;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.bean.model.PageableCriteria;
import com.mdvns.mdvn.common.bean.model.ProjectDetail;
import com.mdvns.mdvn.common.bean.model.TerseInfo;
import com.mdvns.mdvn.common.bean.model.TerseTemplate;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.*;
import com.mdvns.mdvn.project.config.WebConfig;
import com.mdvns.mdvn.project.domain.entity.Project;
import com.mdvns.mdvn.project.repository.LeaderRepository;
import com.mdvns.mdvn.project.repository.ProjectRepository;
import com.mdvns.mdvn.project.repository.TagRepository;
import com.mdvns.mdvn.project.repository.TemplateRepository;
import com.mdvns.mdvn.project.service.RetrieveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class RetrieveServiceImpl implements RetrieveService {

    private static final Logger LOG = LoggerFactory.getLogger(RetrieveServiceImpl.class);

    /*repository 注入*/
    @Resource
    private ProjectRepository projectRepository;

    @Resource
    private LeaderRepository projectStaffRepository;

    @Resource
    private TagRepository projectTagRepository;

    @Resource
    private TemplateRepository projectTemplateRepository;

    /*注入 webConfig*/
    @Resource
    private WebConfig webConfig;

    /**
     * 查询所有数据(只需要返回表数据就够了)：支持分页
     *
     * @return restResponse
     */
    @Override
    @Transactional
    public RestResponse<?> retrieveAll(PageableQueryWithoutArgRequest pageableQueryWithoutArgRequest) {
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
        Page<Project> deptPage = this.projectRepository.findAll(pageRequest);
        //返回结果
        return RestResponseUtil.success(deptPage);
    }

    /**
     * 获取指定serialNo的项目详情
     *
     * @param retrieveDetailRequest request
     * @return restResponse
     */
    @Override
    @Transactional
    public RestResponse<?> retrieveDetailBySerialNo(SingleCriterionRequest retrieveDetailRequest) throws BusinessException {
        LOG.info("获取指定id项目的详情, 开始运行【retrieveDetailById】service...");
        //获取request中的id
        String serialNo = retrieveDetailRequest.getCriterion();
        //根据id查询
        Project proj = this.projectRepository.findBySerialNo(serialNo);
        //数据不存在，抛异常
        MdvnCommonUtil.notExistingError(proj, MdvnConstant.ID, retrieveDetailRequest.getCriterion());
        //设置
        ProjectDetail detail = buildDetail(retrieveDetailRequest.getStaffId(), proj);
        LOG.info("获取指定id项目的详情成功, 结束运行【retrieveDetailById】service...");
        //返回结果
        return RestResponseUtil.success(detail);
    }

    /**
     * 获取指定serialNo的项目的模板Id
     * @param retrieveRequest request
     * @return list
     */
    @Override
    public List<Long> retrieveTemplate(SingleCriterionRequest retrieveRequest) {
        Integer isDeleted = (null==retrieveRequest.getIsDeleted())?MdvnConstant.ZERO:retrieveRequest.getIsDeleted();
        Long projId = this.projectRepository.findIdBySerialNo(retrieveRequest.getCriterion());
        return this.projectTemplateRepository.findTemplatesByProjIdAndIsDeleted(projId, isDeleted);
    }

    /**
     * 根据serialNo获取layerType
     * @param retrieveRequest request
     * @return Integer
     */
    @Override
    public Integer retrieveLayerType(SingleCriterionRequest retrieveRequest) throws BusinessException {
        Project project = this.projectRepository.findBySerialNo(retrieveRequest.getCriterion());
        MdvnCommonUtil.notExistingError(project, ErrorEnum.PROJCET_NOT_EXISTS, "编号为【"+retrieveRequest.getCriterion()+"】的项目不存在.");
        return project.getLayerType();
    }

    /**
     * 根据project构建ProjectDetail
     *
     * @param staffId staffId
     * @param proj    project
     * @return restResponse
     */
    private ProjectDetail buildDetail(Long staffId, Project proj) throws BusinessException {
        ProjectDetail detail = new ProjectDetail();
        //设置id
        if (proj.getId() != null) {
            detail.setId(proj.getId());
        }
        //设置编号
        if (!StringUtils.isEmpty(proj.getSerialNo())) {
            detail.setSerialNo(proj.getSerialNo());
        }
        //设置状态
        if (!StringUtils.isEmpty(proj.getStatus())) {
            detail.setStatus(proj.getStatus());
        }
        //设置进度
        if (proj.getProgress() != null) {
            detail.setProgress(proj.getProgress());
        }
        //设置名称
        if (!StringUtils.isEmpty(proj.getName())) {
            detail.setName(proj.getName());
        }
        //设置描述
        if (!StringUtils.isEmpty(proj.getDescription())) {
            detail.setDescription(proj.getDescription());
        }
        //设置优先级
        if (null != proj.getPriority()) {
            detail.setPriority(proj.getPriority());
        }
        //设置开始、结束日期
        if (null != proj.getStartDate()) {
            detail.setStartDate(proj.getStartDate().getTime());
        }
        if (null != proj.getEndDate()) {
            detail.setEndDate(proj.getEndDate().getTime());
        }
        //设置可调整系数
        if (null != proj.getContingency()) {
            detail.setContingency(proj.getContingency());
        }
        //设置负责人
        detail.setLeaders(getLeaders(staffId, proj.getId()));
        //设置标签
        detail.setTags(getTags(staffId, proj.getId()));
        //设置模板
        detail.setTemplates(getTemplates(staffId, proj.getId()));
        //设置需求列表
//        detail.setRequirements(getRequirements(staffId, proj.getSerialNo()));
        //设置附件
        detail.setAttchInfos(FileUtil.getAttaches(proj.getSerialNo()));
        return detail;
    }

    /**
     * 查询指定项目的标签
     *
     * @param staffId staff
     * @param projId  project
     * @return restResponse
     */
    private List<TerseInfo> getTags(Long staffId, Long projId) throws BusinessException {
        LOG.info("查询指定项目的标签, 开始运行【getTags】service...");
        //获取指定项目的模板人id
        List<Long> ids = this.projectTagRepository.findTagsByProjIdAndIsDeleted(projId, MdvnConstant.ZERO);
        //构建获取指定项目标签url
        String retrieveTagsUrl = webConfig.getRetrieveTagsUrl();
        //调用tag模块获取标签信息
        return RestTemplateUtil.retrieveTerseInfo(staffId, ids, retrieveTagsUrl);
    }

    /**
     * 获取指定项目的模板
     *
     * @param staffId staff
     * @param projId  project
     * @return restResponse
     */
    private List<TerseTemplate> getTemplates(Long staffId, Long projId) throws BusinessException {
        LOG.info("获取指定项目的模板, 开始运行【getTemplates】service...");
        //获取指定项目的模板id
        List<Long> ids = this.projectTemplateRepository.findTemplatesByProjIdAndIsDeleted(projId, MdvnConstant.ZERO);
        if (ids.isEmpty()) {
            return null;
        }
        //构建获取指定项目模板url
        String retrieveTemplatesUrl = webConfig.getRetrieveTemplatesUrl();
        //调用template模块获取负责人信息
        LOG.info("查询terseInfo的url是：【{}】", retrieveTemplatesUrl);
        //实例化restTemplate对象
        RestTemplate restTemplate = new RestTemplate();
        //构建ParameterizedTypeReference
        ParameterizedTypeReference<RestResponse<TerseTemplate[]>> typeRef = new ParameterizedTypeReference<RestResponse<TerseTemplate[]>>() {
        };
        //构建requestEntity
        HttpEntity<?> requestEntity = new HttpEntity<>(new RetrieveTerseInfoRequest(staffId, ids));
        //构建responseEntity
        ResponseEntity<RestResponse<TerseTemplate[]>> responseEntity = restTemplate.exchange(retrieveTemplatesUrl,
                HttpMethod.POST, requestEntity, typeRef);
        //获取restResponse
        RestResponse<TerseTemplate[]> restResponse = responseEntity.getBody();
        if (!MdvnConstant.SUCCESS_CODE.equals(restResponse.getCode())) {
            LOG.error("获取指定id集合的TerseInfo失败.");
            throw new BusinessException(ErrorEnum.GET_BASE_INFO_FAILED, "获取指定id集合的TerseInfo失败.");
        }
        return Arrays.asList(restResponse.getData());
    }

    /**
     * 获取指定项目的负责人
     *
     * @param staffId staff
     * @param projId  project
     * @return restResponse
     */
    private List<TerseInfo> getLeaders(Long staffId, Long projId) throws BusinessException {
        LOG.info("获取指定项目的负责人, 开始运行【getLeaders】service...");
        //获取指定项目的负责人id
        List<Long> ids = this.projectStaffRepository.findLeadersByProjIdAndIsDeleted(projId, MdvnConstant.ZERO);
        //如果负责人
        if (ids.isEmpty()) return null;
        //构建获取指定项目负责人url
        String retrieveLeadersUrl = webConfig.getRetrieveLeadersUrl();
        //调用staff模块获取负责人信息
        return RestTemplateUtil.retrieveTerseInfo(staffId, ids, retrieveLeadersUrl);
    }

    /**
     * 获取指定项目id的需求列表
     *
     * @param staffId      staff
     * @param projSerialNo projSerialNo
     * @return restResponse
     */
    /*private List<RequirementModel> getRequirements(Long staffId, String projSerialNo) throws BusinessException {
        //实例化restTem对象
        RestTemplate restTemplate = new RestTemplate();
        //构建retrieveRequirementsUrl
        String retrieveRequirementsUrl = webConfig.getRetrieveRequirementsUrl();
        //构建ParameterizedTypeReference
        ParameterizedTypeReference<RestResponse<PageableResponse<RequirementModel>>> typeReference = new ParameterizedTypeReference<RestResponse<PageableResponse<RequirementModel>>>() {
        };
        //构建requestEntity
        HttpEntity<?> requestEntity = new HttpEntity<>(new SingleCriterionRequest(staffId, projSerialNo));
        //构建responseEntity
        ResponseEntity<RestResponse<PageableResponse<RequirementModel>>> responseEntity = restTemplate.exchange(retrieveRequirementsUrl, HttpMethod.POST, requestEntity, typeReference);
        //构建restResponse
        RestResponse<PageableResponse<RequirementModel>> restResponse = responseEntity.getBody();
        //如果code不是“000”, 抛出异常
        if (!MdvnConstant.SUCCESS_CODE.equals(restResponse.getCode())) {
            LOG.error("获取指定项目的需求列表失败: {}", restResponse.getMsg());
            throw new BusinessException(restResponse.getCode(), restResponse.getMsg());
        }
        return restResponse.getData().getContent();
    }*/

}
