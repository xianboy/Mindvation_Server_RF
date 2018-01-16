package com.mdvns.mdvn.template.service.impl;

import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.template.domain.CreateMvpTemplateRequest;
import com.mdvns.mdvn.template.domain.entity.MvpTemplate;
import com.mdvns.mdvn.template.repository.MvpRepository;
import com.mdvns.mdvn.template.service.MvpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class MvpServiceImpl implements MvpService {
    private static final Logger LOG = LoggerFactory.getLogger(MvpServiceImpl.class);

    @Resource
    private MvpRepository repository;

    /**
     * 创建MVP
     *
     * @param creatorId         当前用户ID
     * @param hostSerialNo      MVP所依赖的主体(模板/项目)编号
     * @param createRequest createRequest
     * @return MvpTemplate
     */
    @Override
    public MvpTemplate create(Long creatorId, Long templateId, String hostSerialNo, CreateMvpTemplateRequest createRequest) {
        MvpTemplate mvpTemplate = new MvpTemplate();
        mvpTemplate.setCreatorId(creatorId);
        mvpTemplate.setHostSerialNo(hostSerialNo);
        mvpTemplate.setMvpIndex(createRequest.getMvpIndex());
        mvpTemplate.setTemplateId(templateId);
        return this.repository.saveAndFlush(mvpTemplate);

    }

    /**
     * 获取指定serialNo的模板的迭代计划
     * @param retrieveRequest request
     * @return List
     */
    @Override
    public List<MvpTemplate> retrieveMvpTemplates(SingleCriterionRequest retrieveRequest) {
        Integer isDeleted = (null == retrieveRequest.getIsDeleted()) ? MdvnConstant.ZERO : retrieveRequest.getIsDeleted();
        return this.repository.findDistinctByHostSerialNoAndIsDeleted(retrieveRequest.getCriterion(), isDeleted);
    }

}
