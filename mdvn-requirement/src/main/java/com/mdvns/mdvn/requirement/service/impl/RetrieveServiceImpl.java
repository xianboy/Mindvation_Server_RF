package com.mdvns.mdvn.requirement.service.impl;

import com.mdvns.mdvn.common.bean.*;
import com.mdvns.mdvn.common.bean.model.*;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.*;
import com.mdvns.mdvn.requirement.config.WebConfig;
import com.mdvns.mdvn.requirement.domain.entity.Requirement;
import com.mdvns.mdvn.requirement.repository.MemberRepository;
import com.mdvns.mdvn.requirement.repository.RequirementRepository;
import com.mdvns.mdvn.requirement.service.MemberService;
import com.mdvns.mdvn.requirement.service.RetrieveService;
import com.mdvns.mdvn.requirement.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RetrieveServiceImpl implements RetrieveService {

    private static final Logger LOG = LoggerFactory.getLogger(RetrieveServiceImpl.class);

    @Resource
    private RequirementRepository repository;

    @Resource
    private MemberRepository memberRepository;

    @Resource
    private TagService tagService;

    @Resource
    private MemberService memberService;

    @Resource
    private WebConfig webConfig;


    /**
     * 查询指定project下的requirement列表:支持分页
     *
     * @param byProjIdRequest request
     * @return restResponse
     */
    @Override
    public RestResponse<?> retrieveListByHostSerialNo(SingleCriterionRequest byProjIdRequest) {
        //获取request中的project编号
        String hostSerialNo = byProjIdRequest.getCriterion();

        PageableCriteria pageableCriteria = byProjIdRequest.getPageableCriteria();
        PageRequest pageRequest;
        //构建分页对象
        if (pageableCriteria == null) {
            pageRequest = PageableQueryUtil.defaultPageReqestBuilder();
        } else {
            pageRequest = PageableQueryUtil.pageRequestBuilder(pageableCriteria);
        }
        LOG.info("hostSerialNo:{},分页参数:{}", hostSerialNo, pageRequest.toString());
        //执行分页查询
        Page<Requirement> requirementPage = this.repository.findByHostSerialNo(hostSerialNo, pageRequest);
        //构建分页response
        return RestResponseUtil.success(requirementPage);
    }

    /**
     * 根据id获取需求详情
     *
     * @param retrieveDetailRequest request
     * @return restResponse
     */
    @Override
    public RestResponse<?> retrieveDetailBySerialNo(SingleCriterionRequest retrieveDetailRequest) throws BusinessException {
        LOG.info("获取指定id需求的详情, 开始运行【retrieveDetailById】service...");
        //获取request中的id
        String serialNo = retrieveDetailRequest.getCriterion();
        //根据id查询
        Requirement requirement = this.repository.findBySerialNo(serialNo);
        //数据不存在，抛异常
        MdvnCommonUtil.notExistingError(requirement, MdvnConstant.ID, retrieveDetailRequest.getCriterion());
        //设置
        RequirementDetail detail = buildDetail(retrieveDetailRequest.getStaffId(), requirement);
        //获取用户权限信息
        List<StaffAuthInfo> staffAuthInfos = StaffAuthUtil.rtrvStaffAuthInfo(webConfig.getRtrvStaffAuthUrl(),requirement.getHostSerialNo(),requirement.getSerialNo(),retrieveDetailRequest.getStaffId());
        detail.setStaffAuthInfo(staffAuthInfos);
        LOG.info("获取指定id项目的详情成功, 结束运行【retrieveDetailById】service...");
        //返回结果
        return RestResponseUtil.success(detail);
    }

    /**
     * 获取指定编号的需求的成员
     *
     * @param retrieveRequest request
     * @return RestResponse
     */
    @Override
    public RestResponse<?> retrieveMember(SingleCriterionRequest retrieveRequest) throws BusinessException {
        //赋值isDeleted
        Integer isDeleted = (null == retrieveRequest.getIsDeleted()) ? MdvnConstant.ZERO : retrieveRequest.getIsDeleted();
        //根据编号查询requirement
        Requirement requirement = this.repository.findBySerialNo(retrieveRequest.getCriterion());
        //获取requirement的成员
        List<RoleMember> members = this.memberService.getRoleMembers(retrieveRequest.getStaffId(), requirement.getId(), requirement.getTemplateId(), isDeleted);
        //构建返回值
        return RestResponseUtil.success(members);
    }

    /**
     * 获取指定编号的需求的过程方法id
     * @param retrieveRequest request
     * @return Long
     */
    @Override
    public Long retrieveLabelIdBySerialNo(SingleCriterionRequest retrieveRequest) {
        Integer isDeleted = (null==retrieveRequest.getIsDeleted())?MdvnConstant.ZERO:retrieveRequest.getIsDeleted();
        return this.repository.findLabelIdBySerialNoAndIsDeleted(retrieveRequest.getCriterion(), isDeleted);
    }

    /**
     * 构建需求详情
     *
     * @param staffId     当前用户id
     * @param requirement requirement
     * @return requirementDetail
     */
    private RequirementDetail buildDetail(Long staffId, Requirement requirement) throws BusinessException {
        RequirementDetail detail = new RequirementDetail();
        //设置id
        detail.setId(requirement.getId());
        /*设置模板id*/
        detail.setTemplateId(requirement.getTemplateId());
        //设置上层主体编号
        detail.setHostSerialNo(requirement.getHostSerialNo());
        //设置编号
        detail.setSerialNo(requirement.getSerialNo());
        //设置状态
        detail.setStatus(requirement.getStatus());
        //设置进度
        detail.setProgress(requirement.getProgress());
        //设置概要
        detail.setSummary(requirement.getSummary());
        //设置描述
        detail.setDescription(requirement.getDescription());
        //设置标签
        detail.setTags(getTags(staffId, requirement.getId()));
        //设置优先级
        detail.setPriority(requirement.getPriority());
        //设置过程方法
        detail.setLabel(getLabel(staffId, requirement.getFunctionLabelId()));
        //设置需求创建人对象信息
        String retrieveByIdUrl = webConfig.getRtrvStaffInfoByIdUrl();
        Staff staffInfo = StaffUtil.rtrvStaffInfoById(requirement.getCreatorId(),retrieveByIdUrl);
        detail.setCreatorInfo(staffInfo);
        //设置成员
        detail.setRoleMembers(getRoleMembers(staffId, requirement.getId(), requirement.getTemplateId()));
        //设置开始/结束日期
        detail.setStartDate(requirement.getStartDate().getTime());
        detail.setEndDate(requirement.getEndDate().getTime());
        detail.setStories(getStories(staffId, requirement.getSerialNo()));
        //设置story point 总数
//        detail.setStoryPointAmount(getStoryPointAmount());
        //设置附件
        detail.setAttchInfos(FileUtil.getAttaches(requirement.getSerialNo()));
        //设置评论
        detail.setCommentDetails(this.rtrvCommentInfos(requirement));
        //设置层级结构类型
        detail.setLayerType(requirement.getLayerType());
        return detail;
    }

    /**
     * 查询指定编号的需求下的Story列表
     *
     * @param staffId  staffId
     * @param serialNo 需求编号
     * @return List
     */
    private PageableResponse<Story> getStories(Long staffId, String serialNo) throws BusinessException {
        //实例化restTem对象
        RestTemplate restTemplate = new RestTemplate();
        //构建retrieveRequirementsUrl
        String retrieveStoriesUrl = webConfig.getRetrieveStoriesUrl();
        //构建ParameterizedTypeReference
        ParameterizedTypeReference<RestResponse<PageableResponse<Story>>> typeReference = new ParameterizedTypeReference<RestResponse<PageableResponse<Story>>>() {
        };
        //构建requestEntity
        HttpEntity<?> requestEntity = new HttpEntity<>(new SingleCriterionRequest(staffId, serialNo));
        //构建responseEntity
        ResponseEntity<RestResponse<PageableResponse<Story>>> responseEntity = restTemplate.exchange(retrieveStoriesUrl, HttpMethod.POST, requestEntity, typeReference);
        //构建restResponse
        RestResponse<PageableResponse<Story>> restResponse = responseEntity.getBody();
        //如果code不是“000”, 抛出异常
        if (!MdvnConstant.SUCCESS_CODE.equals(restResponse.getCode())) {
            LOG.error("获取指定项目的需求列表失败: {}", restResponse.getMsg());
            throw new BusinessException(restResponse.getCode(), restResponse.getMsg());
        }
        return restResponse.getData();
    }

    /**
     * 获取指定reqmntId 的角色成员
     *
     * @return list
     */
    private List<RoleMember> getRoleMembers(Long staffId, Long requirementId, Long templateId) throws BusinessException {
        LOG.info("获取指定需求成员, 开始运行【getRoleMembers】service...");
        return this.memberService.getRoleMembers(staffId, requirementId, templateId, MdvnConstant.ZERO);
    }

    /**
     * 根据id获取FunctionLabel
     *
     * @param staffId         staffId
     * @param functionLabelId labelId
     * @return baseInfo
     * @throws BusinessException exception
     */
    private TerseInfo getLabel(Long staffId, Long functionLabelId) throws BusinessException {
        LOG.info("根据id获取FunctionLabel开始...");
        List<Long> ids = new ArrayList<>();
        ids.add(functionLabelId);
        String retrieveLabelUrl = webConfig.getRetrieveLabelUrl();
        List<TerseInfo> list;
        try {
            list = RestTemplateUtil.retrieveTerseInfo(staffId, ids, retrieveLabelUrl);
        } catch (BusinessException ex) {
            LOG.error("获取FunctionLabel失败:【{}】", ex.getMsg());
            throw ex;
        }
        //如果list为空, 抛出异常
        MdvnCommonUtil.emptyList(list, ErrorEnum.FUNCTION_LABEL_NOT_EXISTS, "id为【" + functionLabelId + "】的FunctionLabel不存在...");
        LOG.info("根据id获取FunctionLabel成功...");
        return list.get(MdvnConstant.ZERO);
    }

    /**
     * 获取指定reqmntId的标签
     *
     * @param staffId  staffId
     * @param reqmntId reqmntId
     * @return list
     * @throws BusinessException exception
     */
    private List<TerseInfo> getTags(Long staffId, Long reqmntId) throws BusinessException {
        LOG.info("查询指定需求的标签, 开始运行【getTags】service...");
        //获取指定项目的模板人id
        List<Long> ids = this.tagService.getTags(reqmntId, MdvnConstant.ZERO);
        if (ids.isEmpty()) {
            LOG.info("ID为【{}】的需求无标签.", reqmntId);
            return null;
        }
        //构建获取指定项目标签url
        String retrieveTagsUrl = webConfig.getRetrieveTagsUrl();
        //调用tag模块获取负责人信息
        List<TerseInfo> tags = RestTemplateUtil.retrieveTerseInfo(staffId, ids, retrieveTagsUrl);
        MdvnCommonUtil.emptyList(tags, ErrorEnum.TAG_NOT_EXISTS, "id为【" + ids.toString() + "】的Tag不存在...");
        LOG.info("查询指定需求的标签成功.");
        return tags;
    }

    /**
     * 获取指定serialNo的requirement的不重复成员Id,以及创建者
     * @param singleCriterionRequest request
     * @return restResponse
     * @throws BusinessException exception
     */
    @Override
    public List<Long> retrieveReqMembersBySerialNo(SingleCriterionRequest singleCriterionRequest) throws BusinessException {
        //根据编号查询requirement
        Requirement requirement = this.repository.findBySerialNo(singleCriterionRequest.getCriterion());
        Long staffId = singleCriterionRequest.getStaffId();
        Long creatorId = requirement.getCreatorId();
        Long requirementId = requirement.getId();
        Long templateId = requirement.getTemplateId();
        List<RoleMember> roleMembers = this.memberService.getRoleMembers(staffId, requirementId, templateId, 0);
        List<Long> memberIds = StaffUtil.getDistinctMembers(roleMembers);
        if (!memberIds.contains(creatorId)) {
            memberIds.add(creatorId);
        }
        return memberIds;
    }

    /**
     * 获取指定serialNo的requirement的不重复成员对象
     * @param singleCriterionRequest
     * @return
     */
    @Override
    public RestResponse<?> retrieveReqMembersInfoBySerialNo(SingleCriterionRequest singleCriterionRequest) throws BusinessException {
        LOG.info("获取所有requirement的成员信息开始...");
        String projSerialNo = singleCriterionRequest.getCriterion();
        Long staffId = singleCriterionRequest.getStaffId();
        /*在requirement_member表里查出所有不重复的id*/
        List<Long> allDistinctMemberIds = this.memberRepository.findAllDistinctMembers(projSerialNo);
        if (allDistinctMemberIds.isEmpty()) {
            LOG.info("project暂无成员.", allDistinctMemberIds);
            return null;
        }
        //BigInteger转Long
        List<Long> staffIds = new ArrayList<>();
        for (int i = 0; i < allDistinctMemberIds.size(); i++) {
            Long sId = Long.parseLong(String.valueOf(allDistinctMemberIds.get(i)));
            staffIds.add(sId);
        }
        //获取member的url
        String retrieveMembersUrl = webConfig.getRetrieveStaffInfosUrl();
        //调用staff模块获取成员信息
        List<Staff> members = RestTemplateUtil.retrieveStaffInfos(staffId, staffIds, retrieveMembersUrl);
        MdvnCommonUtil.emptyList(members, ErrorEnum.STAFF_NOT_EXISTS, "Id为【" + staffIds.toString() + "】的用户不存在.");
        LOG.info("获取所有requirement的成员信息成功...");
        return RestResponseUtil.success(members);
    }

    /**
     * 返回需求的评论list
     *
     * @param requirement
     * @return
     */
    private List<CommentDetail> rtrvCommentInfos(Requirement requirement) {
        String rCommentInfosUrl = webConfig.getRtrvCommentInfosUrl();
        String rtrvStaffInfoByIdUrl = webConfig.getRtrvStaffInfoByIdUrl();
        String projSerialNo = requirement.getHostSerialNo();
        String serialNo = requirement.getSerialNo();
        List<CommentDetail> comDetails = CommentUtil.rtrvCommentInfos(projSerialNo,serialNo,rCommentInfosUrl,rtrvStaffInfoByIdUrl);
        return comDetails;
    }

    /**
     * 获取指定id的过程方法对应的需求编号
     * @param retrieveRequest request
     * @return List
     */
    @Override
    public List<String> retrieveSerialNoByLabel(RetrieveReqmtByLabelRequest retrieveRequest) {
        Integer isDeleted = (null == retrieveRequest.getIsDeleted()) ? MdvnConstant.ZERO : retrieveRequest.getIsDeleted();
        return this.repository.findSerialNoByHostSerialNoAndFunctionLabelIdInAndIsDeleted(retrieveRequest.getHostSerialNo(), retrieveRequest.getLabels(), isDeleted);
    }

    /**
     * 获取指定hostSerialNo(项目编号)和templateId的需求编号
     * @param retrieveRequest request
     * @return List
     */
    @Override
    public List<String> retrieveSerialNo(RetrieveReqmtSerialNoRequest retrieveRequest) {
        LOG.info("获取hostSerialNo为【{}】和templateId为【{}】的需求编号开始...", retrieveRequest.getHostSerialNo(),retrieveRequest.getTemplateId());
        Integer isDeleted = (null == retrieveRequest.getIsDeleted()) ? MdvnConstant.ZERO : retrieveRequest.getIsDeleted();
        List<String> result = this.repository.findSerialNoByHostSerialNoAndTemplateIdAndIsDeleted(retrieveRequest.getHostSerialNo(), retrieveRequest.getTemplateId(), isDeleted);
        LOG.info("成功获取hostSerialNo为【{}】和templateId为【{}】的需求编号:【{}】", retrieveRequest.getHostSerialNo(),retrieveRequest.getTemplateId(), result);
        return result;
    }


    /**
     * 获取以requirement为内容的Dashboard
     * @param retrieveRequest request
     * @return ReqmtDashboard
     */
    public com.mdvns.mdvn.requirement.domain.ReqmtDashboard retrieveDashboard(RetrieveMvpContentRequest retrieveRequest) {
        Integer isDeleted = (null == retrieveRequest.getIsDeleted()) ? MdvnConstant.ZERO : retrieveRequest.getIsDeleted();
        com.mdvns.mdvn.requirement.domain.ReqmtDashboard dashboard = new com.mdvns.mdvn.requirement.domain.ReqmtDashboard();
        dashboard.setBacklogs(getBacklogs(retrieveRequest.getStaffId(), retrieveRequest.getSerialNoList(), isDeleted));
        Long mvpId = retrieveRequest.getTop2MvpId().get(MdvnConstant.ZERO);
        dashboard.setCurrentMvp(getMvpContent(retrieveRequest.getStaffId(), retrieveRequest.getSerialNoList(), mvpId, isDeleted));
        if (null == retrieveRequest.getTop2MvpId().get(MdvnConstant.ONE)) {
            dashboard.setNextMvp(null);
        } else {
            dashboard.setNextMvp(getMvpContent(retrieveRequest.getStaffId(), retrieveRequest.getSerialNoList(), retrieveRequest.getTop2MvpId().get(MdvnConstant.ONE), isDeleted));
        }
        return dashboard;
    }

    /**
     * 获取指定需求集合中mvpId为指定值的数据
     * @param staffId 当前用户Id
     * @param hostSerialNoList 需求集合
     * @param mvpId mvpId
     * @param isDeleted isDeleted
     * @return List
     */
    private List<Requirement> getMvpContent(Long staffId, List<String> hostSerialNoList, Long mvpId, Integer isDeleted) {
        return this.repository.findBySerialNoInAndIsDeletedAndMvpId(hostSerialNoList, isDeleted, mvpId);
    }

    /**
     * 获取指定需求集合中的mvpId为空的数据
     * @param staffId 当前用户Id
     * @param hostSerialNoList 需求集合
     * @param isDeleted isDeleted
     * @return List
     */
    private List<Requirement> getBacklogs(Long staffId, List<String> hostSerialNoList, Integer isDeleted) {
        return this.repository.findBySerialNoInAndIsDeletedAndMvpIdIsNull(hostSerialNoList, isDeleted);
    }

}
