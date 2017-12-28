package com.mdvns.mdvn.template.service.impl;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.MdvnCommonUtil;
import com.mdvns.mdvn.common.util.RestResponseUtil;
import com.mdvns.mdvn.template.domain.CreateTemplateRequest;
import com.mdvns.mdvn.template.domain.entity.Delivery;
import com.mdvns.mdvn.template.domain.entity.FunctionLabel;
import com.mdvns.mdvn.template.domain.entity.IterationTemplate;
import com.mdvns.mdvn.template.domain.entity.Template;
import com.mdvns.mdvn.template.repository.LabelRepository;
import com.mdvns.mdvn.template.repository.TemplateRepository;
import com.mdvns.mdvn.template.service.CreateService;
import com.mdvns.mdvn.template.service.DeliveryService;
import com.mdvns.mdvn.template.service.LabelService;
import com.mdvns.mdvn.template.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CreateServiceImpl implements CreateService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateServiceImpl.class);

    @Autowired
    private TemplateRepository tmplRepository;

    @Resource
    private LabelService labelService;

    @Resource
    private DeliveryService deliveryService;

    @Resource
    private RoleService roleService;

    /**
     * 新建模板
     *
     * @param createRequest 新建request
     * @return 新建模板
     */
    @Override
    @Transactional
    public RestResponse<?> create(CreateTemplateRequest createRequest) throws BusinessException {
        LOG.info("创建模板开始...");
        Long creatorId = createRequest.getCreatorId();
        //根据name查询模板
        Template template = this.tmplRepository.findByName(createRequest.getName());
        //指定name的模板如果存在，抛出异常
        MdvnCommonUtil.existingError(template, "name", createRequest.getName());
        //保存template
        template = createByRequest(createRequest);
        //处理functionLabel
        createLabels(creatorId, template.getSerialNo(), createRequest.getLabels());
        //处理roles
        createRoles(creatorId, template.getSerialNo(), createRequest.getRoleNames());
        //处理迭代模板itTemplates
        handleItTemplates(creatorId, template.getId(), createRequest.getItTemplates());
        ////处理deliverables
        createDeliveries(creatorId, template.getSerialNo(), createRequest.getDeliverables());
        LOG.info("创建模板成功...");
        return RestResponseUtil.success(template);
    }

    /**
     * 创建交付件
     *
     * @param creatorId    creatorId
     * @param hostSerialNo hostSerialNo
     * @param deliveries   deliveries
     */
    private void createDeliveries(Long creatorId, String hostSerialNo, List<Delivery> deliveries) {
        this.deliveryService.create(creatorId, hostSerialNo, deliveries);
    }

    /**
     * @param creatorId   creatorId
     * @param templateId  templateId
     * @param itTemplates itTemplates
     */
    private void handleItTemplates(Long creatorId, Long templateId, List<IterationTemplate> itTemplates) {
        for (IterationTemplate itTemplate : itTemplates) {
            itTemplate.setCreatorId(creatorId);
            itTemplate.setTemplateId(templateId);
        }
    }

    /**
     * 为模板创建角色
     *
     * @param creatorId    creatorId
     * @param hostSerialNo hostSerialNo
     * @param roles        roles
     */
    private void createRoles(Long creatorId, String hostSerialNo, List<String> roles) {
        this.roleService.createRoles(creatorId, hostSerialNo, roles);
    }

    /**
     * 创建过程方法
     *
     * @param creatorId    创建人id
     * @param hostSerialNo 上层对象编号
     * @param labels       模板集合
     */
    private void createLabels(Long creatorId, String hostSerialNo, List<FunctionLabel> labels) throws BusinessException {
        //遍历labels
        for (FunctionLabel label : labels) {
            this.labelService.create(creatorId, hostSerialNo, label);
        }
    }

    /**
     * 根据request保存template
     *
     * @param createRequest request
     * @return 新建模板
     */
    private Template createByRequest(CreateTemplateRequest createRequest) {
        Template template = new Template();
        template.setName(createRequest.getName());
        template.setCreatorId(createRequest.getCreatorId());
        template.setIndustryId(createRequest.getIndustryId());
        template.setStyle(createRequest.getStyle());
        template.setSerialNo(buildSerialNo());
        return this.tmplRepository.saveAndFlush(template);
    }

    /**
     * 构建template编号
     *
     * @return 编号
     */
    private String buildSerialNo() {
        //查询表中的最大id  maxId
        Long maxId = this.tmplRepository.getMaxId();
        //如果表中没有数据，则给maxId赋值为0
        if (maxId == null) {
            maxId = Long.valueOf(MdvnConstant.ZERO);
        }
        maxId += MdvnConstant.ONE;
        return MdvnConstant.TL + maxId;
    }
}
