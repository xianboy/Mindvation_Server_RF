package com.mdvns.mdvn.template.service.impl;

import com.mdvns.mdvn.common.bean.CustomFunctionLabelRequest;
import com.mdvns.mdvn.common.bean.model.TerseInfo;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.ConvertObjectUtil;
import com.mdvns.mdvn.common.util.MdvnCommonUtil;
import com.mdvns.mdvn.template.domain.CreateLabelRequest;
import com.mdvns.mdvn.template.domain.entity.FunctionLabel;
import com.mdvns.mdvn.template.repository.LabelRepository;
import com.mdvns.mdvn.template.service.LabelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    public FunctionLabel create(Long creatorId, String hostSerialNo, CreateLabelRequest labelRequest) throws BusinessException {
        //构建并保存label
        FunctionLabel label = new FunctionLabel();
        label.setCreatorId(creatorId);
        label.setHostSerialNo(hostSerialNo);
        label.setSerialNo(buildSerialNo());
        label = this.labelRepository.saveAndFlush(label);
        List<String> subLabels = labelRequest.getSubLabels();
        //如果子过程方法存在
        if ((null == subLabels||subLabels.isEmpty())) {
            LOG.error("子过程方法不能为空...");
            throw new BusinessException(ErrorEnum.SUB_LABEL_IS_NULL, "新建模板时, 子过程方法不能为空...");
        }
        //保存子过程方法
        List<FunctionLabel> subLabelList = createSubLabels(creatorId, label.getSerialNo(), subLabels);
        label.setSubLabels(subLabelList);
        return label;
    }

    /**
     * 保存子过程方法模块
     * @param creatorId creatorId
     * @param hostSerialNo 编号
     * @param subLabels 子模块
     */
    private List<FunctionLabel> createSubLabels(Long creatorId, String hostSerialNo, List<String> subLabels) {
        List<FunctionLabel> subLabelList = new ArrayList<>();
        //遍历subLabels
        for (String name : subLabels) {
            FunctionLabel label = new FunctionLabel();
            label.setName(name);
            label.setCreatorId(creatorId);
            label.setHostSerialNo(hostSerialNo);
            label.setSerialNo(buildSerialNo());
            label = this.labelRepository.saveAndFlush(label);
            subLabelList.add(label);
        }

        return subLabelList;
    }


    /**
     * 获取指定id集合的过程方法
     *
     * @param ids ids
     * @return List
     * @throws BusinessException Exception
     */
    @Override
    public List<TerseInfo> getLabels(List<Long> ids) throws BusinessException {
        LOG.info("获取过程方法信息开始...");
        //查询id、serialNo和name
        List<Object[]> resultSet = this.labelRepository.findTerseInfoById(ids);
        MdvnCommonUtil.emptyList(resultSet, ErrorEnum.FUNCTION_LABEL_NOT_EXISTS, "id为【" + ids.toString() + "】的functionLabel不存在...");
        LOG.info("获取过程方法信息成功...");
        //结果集转换
        return ConvertObjectUtil.convertObjectArray2TerseInfo(resultSet);
    }

    /**
     * 根据hostSerialNo查询FunctionLabel
     * @param hostSerialNo hostSerialNo
     * @param isDeleted isDeleted
     * @return List
     */
    @Override
    public List<FunctionLabel> findByHostSerialNoAndIsDeleted(String hostSerialNo, Integer isDeleted) {
        return this.labelRepository.findByHostSerialNoAndIsDeleted(hostSerialNo, isDeleted);
    }

    /**
     * 获取指定id的模板的FunctionLabel
     * @param hostSerialNo hostSerialNo
     * @param isDeleted isDeleted
     * @return List
     */
    @Override
    public List<TerseInfo> getTemplateLabel(String hostSerialNo, Integer isDeleted) throws BusinessException {
        List<Long> idList = this.labelRepository.findIdByHostSerialNoAndIsDeleted(hostSerialNo, isDeleted);
        if (idList.isEmpty()) {
            return null;
        }
        return getLabels(idList);
    }

    /**
     * 获取指定id的过程方法模块及其子模块
     * @param id id
     * @param isDeleted isDeleted
     * @return FunctionLabel
     */
    @Override
    public FunctionLabel retrieveLabelDetail(Long id, Integer isDeleted) throws BusinessException {
        FunctionLabel label = this.labelRepository.findOne(id);
        if (null == label) {
            LOG.error("ID为【{}】的FunctionLabel不存在.", id);
            throw new BusinessException(ErrorEnum.FUNCTION_LABEL_NOT_EXISTS, "ID为【"+id+"】的FunctionLabel不存在.");
        }
        label.setSubLabels(findByHostSerialNoAndIsDeleted(label.getSerialNo(), isDeleted));
        return label;
    }

    /**
     * 获取指定id的过程方法的子过程方法
     * @param labelId labelId
     * @param isDeleted isDeleted
     * @return RestResponse
     */
    @Override
    public List<TerseInfo> retrieveSubLabel(Long labelId, Integer isDeleted) {
        FunctionLabel label = this.labelRepository.findOne(labelId);
        if (null == label) {
            LOG.info("ID为【{}】的过程方法不存在.", labelId);
            return null;
        }
        List<Object[]> subLabels = this.labelRepository.findTerseInfoByHostSerialNo(label.getSerialNo(), isDeleted);
        return ConvertObjectUtil.convertObjectArray2TerseInfo(subLabels);
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
