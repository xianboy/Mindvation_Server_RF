package com.mdvns.mdvn.story.service.impl;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.RtrvCommentInfosRequest;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.bean.model.*;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.*;
import com.mdvns.mdvn.story.config.WebConfig;
import com.mdvns.mdvn.story.domain.entity.Story;
import com.mdvns.mdvn.story.repository.StoryRepository;
import com.mdvns.mdvn.story.service.MemberService;
import com.mdvns.mdvn.story.service.RetrieveService;
import com.mdvns.mdvn.story.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RetrieveServiceImpl implements RetrieveService {

    private static final Logger LOG = LoggerFactory.getLogger(RetrieveServiceImpl.class);


    @Resource
    private StoryRepository repository;

    @Resource
    private TagService tagService;

    @Resource
    private MemberService memberService;

    @Resource
    private WebConfig webConfig;


    /**
     * 获取指定id的Story的详情
     *
     * @param retrieveDetailRequest request
     * @return restResponse
     */
    @Override
    public RestResponse<?> retrieveDetailById(SingleCriterionRequest retrieveDetailRequest) throws BusinessException {
        LOG.info("获取指定id的story的详情, 开始运行【retrieveDetailById】service...");
        //获取request中的id
        Long id = Long.valueOf(retrieveDetailRequest.getCriterion());
        //根据id查询
        Story story = this.repository.findOne(id);
        //数据不存在，抛异常
        MdvnCommonUtil.notExistingError(story, MdvnConstant.ID, retrieveDetailRequest.getCriterion());
        //设置
        StoryDetail detail = buildDetail(retrieveDetailRequest.getStaffId(), story);
        //获取用户权限信息
        List<StaffAuthInfo> staffAuthInfos = StaffAuthUtil.rtrvStaffAuthInfo(webConfig.getRtrvStaffAuthUrl(),story.getProjSerialNo(),story.getSerialNo(),retrieveDetailRequest.getStaffId());
        detail.setStaffAuthInfo(staffAuthInfos);
        LOG.info("获取指定id的story的详情成功, 结束运行【retrieveDetailById】service...");
        //返回结果
        return RestResponseUtil.success(detail);
    }

    /**
     * 根据hostSerialNo获取story详情
     *
     * @param singleCriterionRequest request
     * @return restResponse
     */
    @Override
    public RestResponse<?> retrieveDetailBySerialNo(SingleCriterionRequest singleCriterionRequest) throws BusinessException {
        LOG.info("获取指定serialNo的story的详情, 开始运行【retrieveDetailBySerialNo】service...");
        //根据
        Story story = this.repository.findBySerialNo(singleCriterionRequest.getCriterion());
        //数据不存在，抛异常
        MdvnCommonUtil.notExistingError(story, ErrorEnum.STORY_NOT_EXISTS, "编号为【" + singleCriterionRequest.getCriterion() + "】的story不存在");
        //设置
        StoryDetail detail = buildDetail(singleCriterionRequest.getStaffId(), story);
        //获取用户权限信息
        List<StaffAuthInfo> staffAuthInfos = StaffAuthUtil.rtrvStaffAuthInfo(webConfig.getRtrvStaffAuthUrl(),story.getProjSerialNo(),story.getSerialNo(),singleCriterionRequest.getStaffId());
        detail.setStaffAuthInfo(staffAuthInfos);
        LOG.info("获取指定serialNo的story的详情成功, 结束运行【retrieveDetailBySerialNo】service...");
        //返回结果
        return RestResponseUtil.success(detail);
    }


    /**
     * 根据上层模块的编号获取Story列表:支持分页
     *
     * @param singleCriterionRequest request
     * @return restResponse
     */
    @Override
    public RestResponse<?> retrieveListByHostSerialNo(SingleCriterionRequest singleCriterionRequest) {
        Integer isDeleted = (null == singleCriterionRequest.getIsDeleted()) ? MdvnConstant.ZERO : singleCriterionRequest.getIsDeleted();
        //获取request中的hostSerialNo
        String hostSerialNo = singleCriterionRequest.getCriterion();
        LOG.info("获取hostSerialNo为【{}】的Story列表开始...", hostSerialNo);
        PageableCriteria pageableCriteria = singleCriterionRequest.getPageableCriteria();
        PageRequest pageRequest;
        //构建分页对象
        if (pageableCriteria == null) {
            pageRequest = PageableQueryUtil.defaultPageReqestBuilder();
        } else {
            pageRequest = PageableQueryUtil.pageRequestBuilder(pageableCriteria);
        }
        //执行分页查询
        Page<Story> storyPage = this.repository.findByHostSerialNoAndIsDeleted(hostSerialNo, isDeleted, pageRequest);
        LOG.info("获取Story列表结束.此页获取Story【{}】条.", storyPage.getTotalElements());
        //构建分页response
        return RestResponseUtil.success(storyPage);
    }

    /**
     * 构建Story对象
     *
     * @param staffId staffId
     * @param story   story
     * @return SotryDetail
     * @throws BusinessException BusinessException
     */
    private StoryDetail buildDetail(Long staffId, Story story) throws BusinessException {
        StoryDetail detail = new StoryDetail();
        //设置id
        detail.setId(story.getId());
        //设置编号
        detail.setSerialNo(story.getSerialNo());
        //设置需求编号
        detail.setHostSerialNo(story.getHostSerialNo());
        //设置项目编号
        detail.setProjSerialNo(story.getProjSerialNo());
        //设置模板编号
        detail.setTemplateId(story.getTemplateId());
        //设置状态
        detail.setStatus(story.getStatus());
        //设置进度
        detail.setProgress(story.getProgress());
        //设置概要
        detail.setSummary(story.getSummary());
        //设置描述
        detail.setDescription(story.getDescription());
        //设置标签
        detail.setTags(getTags(staffId, story.getId(), MdvnConstant.ZERO));
        //设置优先级
        detail.setPriority(story.getPriority());
        //设置过程方法
        detail.setLabel(getLabel(staffId, story.getFunctionLabelId()));
        //设置修改时可选择的子过程方法(上层模块对应的子过程方法)
        detail.setOptionalLabel(getOptionalLabels(staffId, story.getHostSerialNo()));
        //设置需求创建人对象信息
        String retrieveByIdUrl = webConfig.getRtrvStaffInfoByIdUrl();
        Staff staffInfo = StaffUtil.rtrvStaffInfoById(story.getCreatorId(),retrieveByIdUrl);
        detail.setCreatorInfo(staffInfo);
        //设置成员
        detail.setMembers(getRoleMembers(staffId, story.getId(), story.getTemplateId()));
        //设置修改时可选的成员(上层模块的成员)
        detail.setOptionalMembers(getOptionalMembers(staffId, story.getHostSerialNo()));
        //设置开始/结束日期
        detail.setStartDate(story.getStartDate().getTime());
        detail.setEndDate(story.getEndDate().getTime());
        //设置story point
        detail.setStoryPoint(story.getStoryPoint());
        //设置task列表
        detail.setTasks(getTasks(staffId, story.getSerialNo()));
        //设置附件
        detail.setAttchInfos(FileUtil.getAttaches(story.getSerialNo()));
        //设置评论
        detail.setCommentDetails(this.rtrvCommentInfos(story));
        //设置层级结构类型
        detail.setLayerType(story.getLayerType());
        return detail;
    }

    /**
     * 获取Story下的task列表
     *
     * @param staffId  staffId
     * @param serialNo serialNo
     * @return List<Task>
     */
    private List<Task> getTasks(Long staffId, String serialNo) throws BusinessException {
        LOG.info("获取serialNo为【{}】的Story的taskList开始...", serialNo);
        String retrieveTaskListUrl = webConfig.getRetrieveTaskListUrl();
        //实例化restTemplate对象
        RestTemplate restTemplate = new RestTemplate();
        //构建ParameterizedTypeReference
        ParameterizedTypeReference<RestResponse<Task[]>> typeRef = new ParameterizedTypeReference<RestResponse<Task[]>>() {
        };
        //构建requestEntity
        HttpEntity<?> requestEntity = new HttpEntity<>(new SingleCriterionRequest(staffId, serialNo));
        //构建responseEntity
        ResponseEntity<RestResponse<Task[]>> responseEntity = restTemplate.exchange(retrieveTaskListUrl,
                HttpMethod.POST, requestEntity, typeRef);
        //获取restResponse
        RestResponse<Task[]> restResponse = responseEntity.getBody();
        if (!MdvnConstant.SUCCESS_CODE.equals(restResponse.getCode())) {
            LOG.error("获取上层模块的角色成员失败.");
            throw new BusinessException(ErrorEnum.RETRIEVE_ROLEMEMBER_FAILED, "获取上层模块的角色成员失败.");
        }
        if (null == restResponse.getData()) {
            return null;
        }
        LOG.info("获取serialNo为【{}】的Story的taskList成功...", serialNo);
        return Arrays.asList(restResponse.getData());
    }

    /**
     * 获取指定编号的上层模块的角色成员
     *
     * @param staffId      staffId
     * @param hostSerialNo hostSerialNo
     * @return List
     */
    private List<RoleMember> getOptionalMembers(Long staffId, String hostSerialNo) throws BusinessException {
        //获取上层模块的角色成员Url
        String retrieveOptionalMemberUrl = webConfig.getRetrieveOptionalMemberUrl();
        //实例化restTemplate对象
        RestTemplate restTemplate = new RestTemplate();
        //构建ParameterizedTypeReference
        ParameterizedTypeReference<RestResponse<RoleMember[]>> typeRef = new ParameterizedTypeReference<RestResponse<RoleMember[]>>() {
        };
        //构建requestEntity
        HttpEntity<?> requestEntity = new HttpEntity<>(new SingleCriterionRequest(staffId, hostSerialNo));
        //构建responseEntity
        ResponseEntity<RestResponse<RoleMember[]>> responseEntity = restTemplate.exchange(retrieveOptionalMemberUrl,
                HttpMethod.POST, requestEntity, typeRef);
        //获取restResponse
        RestResponse<RoleMember[]> restResponse = responseEntity.getBody();
        if (!MdvnConstant.SUCCESS_CODE.equals(restResponse.getCode())) {
            LOG.error("获取上层模块的角色成员失败.");
            throw new BusinessException(ErrorEnum.RETRIEVE_ROLEMEMBER_FAILED, "获取上层模块的角色成员失败.");
        }
        if (null == restResponse.getData()) {
            return null;
        }
        return Arrays.asList(restResponse.getData());
    }

    /**
     * 获取指定过程方法及其子过程方法.
     *
     * @param staffId      staffId
     * @param hostSerialNo hostSerialNo
     * @return FunctionLabel
     */
    private FunctionLabel getOptionalLabels(Long staffId, String hostSerialNo) throws BusinessException {
        String retrieveHostLabelIdUrl = webConfig.getRetrieveHostLabelIdUrl();
        RestTemplate restTemplate = new RestTemplate();
        //根据上一层模块的编号hostSerialNo获取其对应的过程方法的id
        Long labelId = restTemplate.postForObject(retrieveHostLabelIdUrl, new SingleCriterionRequest(staffId, hostSerialNo), Long.class);
        //查询story自定义的过程方法以及其上层的过程方法对应的所有子过程方法
        return retrieveHostLabelAndSubLabel(staffId, labelId);
    }

    /**
     * 获取指定编号的story上层的过程方法及其所有子过程方法
     *
     * @param staffId staffId
     * @param labelId 上层模块对应的过程方法id
     * @return story上层过程方法及其子过程方法和Story自定义的过程方法
     * @throws BusinessException BusinessException
     */
    private FunctionLabel retrieveHostLabelAndSubLabel(Long staffId, Long labelId) throws BusinessException {
        LOG.info("获取指定编号的story自定义的过程方法以及其上层的过程方法对应的所有子过程方法开始...");
        RestTemplate restTemplate = new RestTemplate();
        String retrieveLabelDetailUrl = webConfig.getRetrieveLabelDetailUrl();
        LOG.info("retrieveLabelAndSubLabelUrl:【{}】.", retrieveLabelDetailUrl);
        ParameterizedTypeReference<RestResponse<FunctionLabel>> typeRef = new ParameterizedTypeReference<RestResponse<FunctionLabel>>() {
        };
        //构建requestEntity
        HttpEntity<?> requestEntity = new HttpEntity<>(new SingleCriterionRequest(staffId, labelId.toString()));
        //构建responseEntity
        ResponseEntity<RestResponse<FunctionLabel>> responseEntity = restTemplate.exchange(retrieveLabelDetailUrl,
                HttpMethod.POST, requestEntity, typeRef);
        RestResponse<FunctionLabel> restResponse = responseEntity.getBody();
        LOG.info("responseCode is【{}】.", restResponse.getCode());
        if (!MdvnConstant.SUCCESS_CODE.equals(restResponse.getCode())) {
            LOG.error("获取上层的过程方法对应的所有子过程方法失败.");
            throw new BusinessException(restResponse.getCode(), "获取上层的过程方法对应的所有子过程方法失败." + restResponse.getMsg());
        }
        LOG.info("获取指定编号的story自定义的过程方法以及其上层的过程方法对应的所有子过程方法成功...");
        return restResponse.getData();
    }

    /**
     * 获取指定storyId 的角色成员
     *
     * @return list
     */
    private List<RoleMember> getRoleMembers(Long staffId, Long storyId, Long templateId) throws BusinessException {
        LOG.info("获取指定story成员, 开始运行【getRoleMembers】service...");
        return this.memberService.getRoleMembers(staffId, storyId, templateId, MdvnConstant.ZERO);
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
        LOG.info("获取story的过程方法开始...");
        List<Long> ids = new ArrayList<>();
        ids.add(functionLabelId);
        String retrieveLabelUrl = webConfig.getRetrieveLabelUrl();
        List<TerseInfo> list;
        try {
            list = RestTemplateUtil.retrieveTerseInfo(staffId, ids, retrieveLabelUrl);
        } catch (Exception ex) {
            LOG.error("获取id为【{}】的FunctionLabel失败...", functionLabelId);
            throw new BusinessException(ErrorEnum.TEMPLATE_SYSTEM_ERROR, "调用Template模块获取获取id为【" + functionLabelId + "】的FunctionLabel失败");
        }
        //如果list为空, 抛出异常
        MdvnCommonUtil.emptyList(list, ErrorEnum.FUNCTION_LABEL_NOT_EXISTS, "id为【" + functionLabelId + "】的FunctionLabel不存在...");
        LOG.info("获取story的过程方法成功...");
        return list.get(MdvnConstant.ZERO);
    }

    /**
     * 获取指定storyId的标签
     *
     * @param staffId staffId
     * @param storyId storyId
     * @return list
     * @throws BusinessException exception
     */
    private List<TerseInfo> getTags(Long staffId, Long storyId, Integer isDeleted) throws BusinessException {
        LOG.info("查询指定story的标签, 开始运行【getTags】service...");
        //获取指定项目的模板人id
        List<Long> ids = this.tagService.getTags(staffId, storyId, isDeleted);
        //构建获取指定项目标签url
        String retrieveTagsUrl = webConfig.getRetrieveTagsUrl();
        //调用tag模块获取负责人信息
        List<TerseInfo> tags = RestTemplateUtil.retrieveTerseInfo(staffId, ids, retrieveTagsUrl);
        MdvnCommonUtil.emptyList(tags, ErrorEnum.TAG_NOT_EXISTS, "id为【" + ids.toString() + "】的Tag不存在...");
        LOG.info("查询指定story的标签成功, 共有标签{}个. " + tags.size());
        return tags;
    }

    /**
     * 获取指定serialNo的Story的不重复成员Id,以及创建者
     *
     * @param singleCriterionRequest request
     * @return restResponse
     * @throws BusinessException exception
     */
    @Override
    public List<Long> retrieveStoryMembersBySerialNo(SingleCriterionRequest singleCriterionRequest) throws BusinessException {
        //根据
        Story story = this.repository.findBySerialNo(singleCriterionRequest.getCriterion());
        Long staffId = singleCriterionRequest.getStaffId();
        Long creatorId = story.getCreatorId();
        Long storyId = story.getId();
        Long templateId = story.getTemplateId();
        List<RoleMember> roleMembers = this.memberService.getRoleMembers(staffId, storyId, templateId, 0);
        List<Long> memberIds = StaffUtil.getDistinctMembers(roleMembers);
        if (!memberIds.contains(creatorId)) {
            memberIds.add(creatorId);
        }
        return memberIds;
    }

    /**
     * 返回STORY的评论list
     *
     * @param story
     * @return
     */
    private List<CommentDetail> rtrvCommentInfos(Story story) {
        String rCommentInfosUrl = webConfig.getRtrvCommentInfosUrl();
        String rtrvStaffInfoByIdUrl = webConfig.getRtrvStaffInfoByIdUrl();
        String projSerialNo = story.getProjSerialNo();
        String serialNo = story.getSerialNo();
        List<CommentDetail> comDetails = CommentUtil.rtrvCommentInfos(projSerialNo,serialNo,rCommentInfosUrl,rtrvStaffInfoByIdUrl);
        return comDetails;
    }

}
