package com.mdvns.mdvn.story.service.impl;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.bean.model.PageableCriteria;
import com.mdvns.mdvn.common.bean.model.RoleMember;
import com.mdvns.mdvn.common.bean.model.StoryDetail;
import com.mdvns.mdvn.common.bean.model.TerseInfo;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.MdvnCommonUtil;
import com.mdvns.mdvn.common.util.PageableQueryUtil;
import com.mdvns.mdvn.common.util.RestResponseUtil;
import com.mdvns.mdvn.common.util.RestTemplateUtil;
import com.mdvns.mdvn.story.config.WebConfig;
import com.mdvns.mdvn.story.domain.entity.Story;
import com.mdvns.mdvn.story.repository.StoryRepository;
import com.mdvns.mdvn.story.service.MemberService;
import com.mdvns.mdvn.story.service.RetrieveService;
import com.mdvns.mdvn.story.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
        MdvnCommonUtil.notExistingError(story, MdvnConstant.ID, singleCriterionRequest.getCriterion());
        //设置
        StoryDetail detail = buildDetail(singleCriterionRequest.getStaffId(), story);
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
        Integer isDeleted = (null==singleCriterionRequest.getIsDeleted())?MdvnConstant.ZERO:singleCriterionRequest.getIsDeleted();
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

    private StoryDetail buildDetail(Long staffId, Story story) throws BusinessException {
        StoryDetail detail = new StoryDetail();
        //设置id
        detail.setId(story.getId());
        //设置编号
        detail.setSerialNo(story.getSerialNo());
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
        //设置成员
        detail.setMembers(getRoleMembers(staffId, story.getId(), story.getTemplateId()));
        //设置开始/结束日期
        detail.setStartDate(story.getStartDate().getTime());
        detail.setEndDate(story.getEndDate().getTime());
        //设置story point
        detail.setStoryPoint(story.getStoryPoint());
        //设置附件
        return detail;
    }

    /**
     * 获取指定reqmntId 的角色成员
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
}
