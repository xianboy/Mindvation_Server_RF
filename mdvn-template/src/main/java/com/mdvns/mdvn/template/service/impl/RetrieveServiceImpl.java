package com.mdvns.mdvn.template.service.impl;

import com.mdvns.mdvn.common.bean.*;
import com.mdvns.mdvn.common.bean.model.PageableCriteria;
import com.mdvns.mdvn.common.bean.model.TerseInfo;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.ConvertObjectUtil;
import com.mdvns.mdvn.common.util.MdvnCommonUtil;
import com.mdvns.mdvn.common.util.PageableQueryUtil;
import com.mdvns.mdvn.common.util.RestResponseUtil;
import com.mdvns.mdvn.template.domain.TerseTemplate;
import com.mdvns.mdvn.template.domain.entity.FunctionLabel;
import com.mdvns.mdvn.template.domain.entity.Industry;
import com.mdvns.mdvn.template.domain.entity.Template;
import com.mdvns.mdvn.template.repository.IndustryRepository;
import com.mdvns.mdvn.template.repository.TemplateRepository;
import com.mdvns.mdvn.template.repository.TemplateRoleRepository;
import com.mdvns.mdvn.template.service.LabelService;
import com.mdvns.mdvn.template.service.RetrieveService;
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
    private TemplateRepository templateRepository;

    @Resource
    private TemplateRoleRepository roleRepository;

    @Resource
    private LabelService labelService;

    @Resource
    private IndustryRepository industryRepository;

    /**
     * 根据industryId查询模板
     *
     * @param criterionRequest request
     * @return RestResponse
     */
    @Override
    public RestResponse<?> retrieveByIndustryId(SingleCriterionRequest criterionRequest) {
        //根据request获取industryId
        Long industryId = Long.valueOf(criterionRequest.getCriterion());
        //查询
        List<Object[]> resultSet = this.templateRepository.findByIndustryId(industryId);
        List<TerseInfo> templates = ConvertObjectUtil.convertObjectArray2TerseInfo(resultSet);
        return RestResponseUtil.success(templates);
    }

    /**
     * 获取全部模板:支持分页
     *
     * @param pageableQueryWithoutArgRequest request
     * @return restResponse
     */
    @Override
    public RestResponse<?> retrieveAll(PageableQueryWithoutArgRequest pageableQueryWithoutArgRequest) {
        //获取分页参数对象
        PageableCriteria pageableCriteria = pageableQueryWithoutArgRequest.getPageableCriteria();
        //构建PageRequest
        PageRequest pageRequest;
        if (null == pageableCriteria) {
            LOG.info("id为[{}]的用户没有填写分页参数，故使用默认分页.", pageableQueryWithoutArgRequest.getStaffId());
            pageRequest = PageableQueryUtil.defaultPageReqestBuilder();
        } else {
            pageRequest = PageableQueryUtil.pageRequestBuilder(pageableCriteria);
        }
        //分页查询
        Page<Template> templatePage = this.templateRepository.findAll(pageRequest);
        //返回结果
        return RestResponseUtil.success(templatePage);
    }

    /**
     * 根据指定id集合查询基本信息
     *
     * @param retrieveTerseInfoRequest retrieveTerseInfoRequest
     * @return restResponse
     */
    @Override
    public RestResponse<?> retrieveTerseInfo(RetrieveTerseInfoRequest retrieveTerseInfoRequest) {
        return RestResponseUtil.success(getTerseTemplate(retrieveTerseInfoRequest.getIds()));
    }

    /**
     * @param ids ids
     * @return List
     */
    private List<TerseTemplate> getTerseTemplate(List<Long> ids) {
        /**/
        List<Template> templates = this.templateRepository.findByIdIn(ids);

        List<TerseTemplate> details = new ArrayList<>();
        for (Template t : templates) {
            TerseTemplate detail = new TerseTemplate();
            Industry industry = this.industryRepository.findOne(t.getIndustryId());
            detail.setIndustry(industry);
            List<Template> list = new ArrayList<>();
            list.add(t);
            detail.setTemplates(list);
            details.add(detail);
        }
        return details;
    }

    /**
     * 根据指定id集合查询TemplateRole基本信息
     *
     * @param retrieveTerseInfoRequest retrieveTerseInfoRequest
     * @return restResponse
     */
    @Override
    public RestResponse<?> retrieveRoleBaseInfo(RetrieveTerseInfoRequest retrieveTerseInfoRequest) throws BusinessException {
        return RestResponseUtil.success(getRoles(retrieveTerseInfoRequest.getIds()));
    }

    /**
     * 获取指定id集合的模板角色
     * @param ids ids
     * @return List
     * @throws BusinessException exception
     */
    private List<TerseInfo> getRoles(List<Long> ids) throws BusinessException {
        LOG.info("获取角色信息开始...");
        List<Object[]> resultSet = this.roleRepository.findTerseInfoById(ids);
        MdvnCommonUtil.emptyList(resultSet, ErrorEnum.TEMPLATE_ROLE_NOT_EXISTS, "模板角色不存在.");
        LOG.info("获取角色信息成功...");
        return ConvertObjectUtil.convertObjectArray2TerseInfo(resultSet);
    }

    /**
     * 根据id集合查询 FunctionLabel
     *
     * @param retrieveTerseInfoRequest retrieveTerseInfoRequest
     * @return restResponse
     */
    @Override
    public RestResponse<?> retrieveLabel(RetrieveTerseInfoRequest retrieveTerseInfoRequest) throws BusinessException {
        List<TerseInfo> labels = this.labelService.getLabels(retrieveTerseInfoRequest.getIds());
        return RestResponseUtil.success(labels);
    }

    /**
     * 根据name和hostSerialNo查询过程方法
     *
     * @param retrieveRequest request
     * @return restResponse
     */
    @Override
    public RestResponse<?> retrieveByNameAndHost(RetrieveByNameAndHostRequest retrieveRequest) {
        List<FunctionLabel> functionLabels = this.labelService.findByHostSerialNoAndIsDeleted(retrieveRequest.getHostSerialNo(), MdvnConstant.ZERO);
        return RestResponseUtil.success(functionLabels.get(MdvnConstant.ZERO));
    }

    /**
     * 根据ID获取
     *
     * @param singleCriterionRequest request
     * @return RestResponse
     */
    public RestResponse<?> retrieveById(SingleCriterionRequest singleCriterionRequest) throws BusinessException {
        //从request中获取isDeleted值
        Integer isDeleted = singleCriterionRequest.getIsDeleted();
        //如果isDeleted为空，则赋值为0
        if (null == isDeleted) {
            isDeleted = MdvnConstant.ZERO;
        }
        //获取指定id的模板
        Long id = Long.valueOf(singleCriterionRequest.getCriterion());
        Template template = this.templateRepository.findOne(id);
        MdvnCommonUtil.notExistingError(template, ErrorEnum.TEMPLATE_NOT_EXISTS, "id为【" + id + "】的模板不存在.");
        //获取模板下的过程方法模块
        template.setFunctionLabels(this.labelService.getTemplateLabel(template.getSerialNo(), isDeleted));
        //获取模板的角色
        List<Long> idList = this.roleRepository.findIdByHostSerialNoAndIsDeleted(template.getSerialNo(), isDeleted);
        if (!idList.isEmpty()) {
            List<TerseInfo> roles = getRoles(idList);
            template.setRoles(roles);
        }
        return RestResponseUtil.success(template);
    }

    /**
     * 获取指定id的过程方法及其子方法
     * @param retrieveRequest request
     * @return RestResponse
     */
    @Override
    public RestResponse<?> retrieveLabelDetail(SingleCriterionRequest retrieveRequest) throws BusinessException {
        Long id = Long.valueOf(retrieveRequest.getCriterion());
        Integer isDeleted = (null==retrieveRequest.getIsDeleted())?MdvnConstant.ZERO:retrieveRequest.getIsDeleted();
        FunctionLabel label = this.labelService.retrieveLabelDetail(id, isDeleted);
        if (null == label) {
            LOG.error("ID为【{}】的FunctionLabel不存在.", id);
            throw new BusinessException(ErrorEnum.FUNCTION_LABEL_NOT_EXISTS, "ID为【"+id+"】的FunctionLabel不存在.");
        }
        return RestResponseUtil.success(label);
    }

    @Override
    public RestResponse<?> retrieveHostLabelAndSubLabel(RetrieveHostLabelAndSublabelRequest retrieveRequest) throws BusinessException {
        Integer isDeleted = (null==retrieveRequest.getIsDeleted())?MdvnConstant.ZERO:retrieveRequest.getIsDeleted();
        //查询指定ID的过程方法的子过程方法
        FunctionLabel label = this.labelService.retrieveLabelDetail(retrieveRequest.getHostLabelId(), isDeleted);
        if (null==label) {
            return null;
        }
        //查询指定hostSerialNo的过程方法
        List<TerseInfo> labels = this.labelService.retrieveSubLabel(retrieveRequest.getHostLabelId(), isDeleted);
        labels.addAll(label.getSubLabels());
        label.setSubLabels(labels);
        return RestResponseUtil.success(label);
    }

}
