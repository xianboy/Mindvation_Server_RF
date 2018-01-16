package com.mdvns.mdvn.template.service.impl;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.util.MdvnCommonUtil;
import com.mdvns.mdvn.common.util.RestResponseUtil;
import com.mdvns.mdvn.template.domain.CreateDeliveryRequest;
import com.mdvns.mdvn.template.domain.CreateLabelRequest;
import com.mdvns.mdvn.template.domain.CreateMvpTemplateRequest;
import com.mdvns.mdvn.template.domain.CreateTemplateRequest;
import com.mdvns.mdvn.template.domain.entity.FunctionLabel;
import com.mdvns.mdvn.template.domain.entity.MvpTemplate;
import com.mdvns.mdvn.template.domain.entity.Template;
import com.mdvns.mdvn.template.repository.TemplateRepository;
import com.mdvns.mdvn.template.service.CreateService;
import com.mdvns.mdvn.template.service.DeliveryService;
import com.mdvns.mdvn.template.service.LabelService;
import com.mdvns.mdvn.template.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CreateServiceImpl implements CreateService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateServiceImpl.class);

    @Resource
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
        //处理迭代模板mvpTemplates
        createMvpTemplates(creatorId, template.getSerialNo(), createRequest.getMvpTemplates());
        ////处理deliverables
        createDeliveries(creatorId, template.getSerialNo(), createRequest.getDeliveries());
        LOG.info("创建模板成功...");
        return RestResponseUtil.success(template);
    }

    /**
     * 创建交付件
     *  @param creatorId    creatorId
     * @param hostSerialNo hostSerialNo
     * @param deliveries   deliveries
     */
    private void createDeliveries(Long creatorId, String hostSerialNo, List<CreateDeliveryRequest> deliveries) {
        this.deliveryService.create(creatorId, hostSerialNo, deliveries);
    }

    /**
     * 处理MVP
     * @param creatorId creatorId
     * @param hostSerialNo hostSerialNo
     * @param itTemplates itTemplates
     */
    private void createMvpTemplates(Long creatorId, String hostSerialNo, List<CreateMvpTemplateRequest> itTemplates) {
        for (CreateMvpTemplateRequest mvpTemplateRequest : itTemplates) {
            MvpTemplate mvpTemplate = new MvpTemplate();
            mvpTemplate.setCreatorId(creatorId);
            mvpTemplate.setHostSerialNo(hostSerialNo);
            mvpTemplate.setMvpIndex(mvpTemplateRequest.getMvpIndex());
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
     *  @param creatorId    创建人id
     * @param hostSerialNo 上层对象编号
     * @param labelRequest 模板集合
     */
    private List<FunctionLabel> createLabels(Long creatorId, String hostSerialNo, List<CreateLabelRequest> labelRequest) throws BusinessException {
        //遍历labels
        List<FunctionLabel> labels = new ArrayList<>();
        for (CreateLabelRequest request : labelRequest) {
            FunctionLabel label = this.labelService.create(creatorId, hostSerialNo, request);
            labels.add(label);
        }
        return labels;
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
