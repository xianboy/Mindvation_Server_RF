package com.mdvns.mdvn.template.service.impl;

import com.mdvns.mdvn.common.bean.CustomFunctionLabelRequest;
import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.RestResponseUtil;
import com.mdvns.mdvn.template.domain.entity.FunctionLabel;
import com.mdvns.mdvn.template.repository.LabelRepository;
import com.mdvns.mdvn.template.service.LabelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LabelServiceImpl implements LabelService {
    private static final Logger LOG = LoggerFactory.getLogger(LabelServiceImpl.class);

    @Resource
    private LabelRepository labelRepository;

    /**
     * 保存自定义过程方法模块
     * @param customRequest customRequest
     * @return restResponse
     */
    @Override
    public Long create(CustomFunctionLabelRequest customRequest) {
        LOG.info("即将保存自定义过程方法模块...");
        //requirement和Story可能会自定义，且它们都是一对一关系
        List<FunctionLabel> functionLabels = this.labelRepository.findByHostSerialNoAndIsDeleted(customRequest.getHostSerialNo(), MdvnConstant.ZERO);
        //如果hostSerialNo对应的label已存在，则删除
        if (!functionLabels.isEmpty()) {
            this.labelRepository.delete(functionLabels);
        }
        FunctionLabel label = new FunctionLabel();
        //设置creatorId
        label.setCreatorId(customRequest.getCreatorId());
        //设置hostSerialNo
        label.setHostSerialNo(customRequest.getHostSerialNo());
        label.setSerialNo(buildSerialNo());
        //设置name
        label.setName(customRequest.getName());
        //保存
        label = this.labelRepository.saveAndFlush(label);
        LOG.info("保存自定义过程方法模块成功...");
        return label.getId();
    }

    @Override
    public FunctionLabel create(Long creatorId, String hostSerialNo, FunctionLabel label) throws BusinessException {
        //构建并保存label
        label.setCreatorId(creatorId);
        label.setHostSerialNo(hostSerialNo);
        label.setSerialNo(buildSerialNo());
        FunctionLabel lbel = this.labelRepository.saveAndFlush(label);
        List<String> subLabels = label.getSubLabels();
        //如果子过程方法存在
        if ((null == subLabels||subLabels.isEmpty())) {
            LOG.error("子过程方法不能为空...");
            throw new BusinessException(ErrorEnum.SUB_LABEL_IS_NULL, "新建模板时, 子过程方法不能为空...");
        }
        //保存子过程方法
        createSubLabels(creatorId, lbel.getId().toString(), label.getSubLabels());
        return null;
    }

    /**
     * 保存子过程方法模块
     * @param creatorId creatorId
     * @param hostSerialNo 编号
     * @param subLabels 子模块
     */
    private void createSubLabels(Long creatorId, String hostSerialNo, List<String> subLabels) {
        //遍历subLabels
        for (String name : subLabels) {
            FunctionLabel label = new FunctionLabel();
            label.setName(name);
            label.setCreatorId(creatorId);
            label.setHostSerialNo(hostSerialNo);
            label.setSerialNo(buildSerialNo());
            this.labelRepository.saveAndFlush(label);
        }
    }

    /**
     *给functionLabel构建编号
     * @return string 编号
     */
    private String buildSerialNo() {
        //查询表中的最大id  maxId
        Long maxId = this.labelRepository.getMaxId();
        //如果表中没有数据，则给maxId赋值为0
        if (maxId == null) {
            maxId = Long.valueOf(MdvnConstant.ZERO);
        }
        maxId += MdvnConstant.ONE;
        return MdvnConstant.FL + maxId;
    }
}
