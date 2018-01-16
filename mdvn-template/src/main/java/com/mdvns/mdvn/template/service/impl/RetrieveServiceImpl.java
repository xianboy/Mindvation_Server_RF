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
import com.mdvns.mdvn.template.domain.entity.*;
import com.mdvns.mdvn.template.repository.DeliveryRepository;
import com.mdvns.mdvn.template.repository.IndustryRepository;
import com.mdvns.mdvn.template.repository.TemplateRepository;
import com.mdvns.mdvn.template.repository.TemplateRoleRepository;
import com.mdvns.mdvn.template.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    @Resource
    private DeliveryRepository deliveryRepository;

    @Resource
    private DeliveryService deliveryService;

    @Resource
    private RoleService roleService;

    @Resource
    private MvpService mvpService;

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
     *
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
     * 根据ID获取详情
     *
     * @param retrieveRequest request
     * @return RestResponse
     */
    public RestResponse<?> retrieveById(SingleCriterionRequest retrieveRequest) throws BusinessException {
        //从request中获取isDeleted值:如果isDeleted为空，则赋值为0
        Integer isDeleted = (null==retrieveRequest.getIsDeleted())?MdvnConstant.ZERO:retrieveRequest.getIsDeleted();
        //获取指定id的模板
        Long id = Long.valueOf(retrieveRequest.getCriterion());
        Template template = this.templateRepository.findOne(id);
        MdvnCommonUtil.notExistingError(template, ErrorEnum.TEMPLATE_NOT_EXISTS, "id为【" + id + "】的模板不存在.");
        //获取模板下的过程方法模块
        template.setLabels(this.labelService.retrieveTemplateLabels(new SingleCriterionRequest(retrieveRequest.getStaffId(), template.getSerialNo(), isDeleted)));
        //根据编号获取模板的角色
        List<TemplateRole> roles = this.roleService.getRoles(new SingleCriterionRequest(retrieveRequest.getStaffId(), template.getSerialNo()));
        template.setRoles(roles);
        return RestResponseUtil.success(template);
    }


    /**
     * 获取指定id的过程方法及其子方法
     *
     * @param retrieveRequest request
     * @return RestResponse
     */
    @Override
    public RestResponse<?> retrieveLabelDetail(SingleCriterionRequest retrieveRequest) throws BusinessException {
        Integer isDeleted = (null == retrieveRequest.getIsDeleted()) ? MdvnConstant.ZERO : retrieveRequest.getIsDeleted();
        FunctionLabel label;
        //建议使用该方法时以Id为参数，但是为了提高服务质量，也作了按serialNo查询的容错
        try {
            Long id = Long.valueOf(retrieveRequest.getCriterion());
            label = this.labelService.retrieveLabelDetailById(id, isDeleted);
            if (null == label) {
                LOG.error("ID为【{}】的FunctionLabel不存在.", id);
                throw new BusinessException(ErrorEnum.FUNCTION_LABEL_NOT_EXISTS, "ID为【" + id + "】的FunctionLabel不存在.");
            }
        } catch (Exception ex) {
            label = this.labelService.retrieveLabelDetailByHostSerialNo(retrieveRequest.getCriterion(), isDeleted);
        }
        return RestResponseUtil.success(label);
    }

    /**
     * 获取指定id的交付件
     *
     * @param retrieveRequest request
     * @return RestResponse
     */
    @Override
    public RestResponse<?> retrieveDelivery(SingleCriterionRequest retrieveRequest) {
        Long deliveryId = Long.valueOf(retrieveRequest.getCriterion());
        Delivery delivery = this.deliveryRepository.findOne(deliveryId);
        return RestResponseUtil.success(delivery);
    }

    /**
     * 获取指定id的模板的交付件
     *
     * @param retrieveRequest request
     * @return RestResponse
     */
    @Override
    public RestResponse<?> retrieveDeliveries(SingleCriterionRequest retrieveRequest) throws BusinessException {
        Integer isDeleted = (null == retrieveRequest.getIsDeleted()) ? MdvnConstant.ZERO : retrieveRequest.getIsDeleted();
        String hostSerialNo = this.templateRepository.getSerialNoById(Long.valueOf(retrieveRequest.getCriterion()));
        if (StringUtils.isEmpty(hostSerialNo)) {
            LOG.error("ID为【{}】的模板不存在...", retrieveRequest.getCriterion());
            throw new BusinessException(ErrorEnum.TEMPLATE_NOT_EXISTS, "ID为【" + retrieveRequest.getCriterion() + "】的模板不存在.");
        }
        List<Delivery> deliveries = this.deliveryService.retrieveDeliveriesByHostSerialNo(hostSerialNo, isDeleted);
        return RestResponseUtil.success(deliveries);
    }

    /**
     * 获取指定ID的模板的迭代计划
     * @param retrieveRequest request
     * @return List<MvpTemplate>
     */
    @Override
    public List<MvpTemplate> retrieveMvpTemplates(SingleCriterionRequest retrieveRequest) {
        String templateSerialNo = this.templateRepository.getSerialNoById(Long.valueOf(retrieveRequest.getCriterion()));
        return this.mvpService.retrieveMvpTemplates(new SingleCriterionRequest(retrieveRequest.getStaffId(),templateSerialNo));
    }

    /**
     * 获取mvpId为指定的值的过程方法的Id
     * @param retrieveRequest request
     * @return List
     */
    @Override
    public List<Long> retrieveLabelByMvp(SingleCriterionRequest retrieveRequest) {
        return this.labelService.retrieveLabelByMvp(retrieveRequest);
    }

    /**
     * 获取指定ID的模板名称
     * @param retrieveRequest request
     * @return 模板名称
     */
    @Override
    public String retrieveTemplateName(SingleCriterionRequest retrieveRequest) {
        return this.templateRepository.findNameById(Long.valueOf(retrieveRequest.getCriterion()));
    }

}
