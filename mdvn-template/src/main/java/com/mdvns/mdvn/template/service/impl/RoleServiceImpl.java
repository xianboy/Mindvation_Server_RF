package com.mdvns.mdvn.template.service.impl;

import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.SingleCriterionRequest;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.util.ConvertObjectUtil;
import com.mdvns.mdvn.common.util.RestResponseUtil;
import com.mdvns.mdvn.template.domain.entity.TemplateRole;
import com.mdvns.mdvn.template.repository.TemplateRepository;
import com.mdvns.mdvn.template.repository.TemplateRoleRepository;
import com.mdvns.mdvn.template.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private TemplateRoleRepository roleRepository;

    @Resource
    private TemplateRepository templateRepository;


    /**
     * 创建模板角色
     * @param creatorId creatorId
     * @param hostSerialNo hostSerialNo
     * @param roles roles
     * @return list
     */
    @Override
    @Transactional
    public List<TemplateRole> createRoles(Long creatorId, String hostSerialNo, List<String> roles) {
        List<TemplateRole> roleList = new ArrayList<>();
        for (String name : roles) {
            TemplateRole role = new TemplateRole();
            role.setCreatorId(creatorId);
            role.setHostSerialNo(hostSerialNo);
            role.setSerialNo(buildSerialNo());
            role.setName(name);
            role = this.roleRepository.saveAndFlush(role);
            roleList.add(role);
        }
        return roleList;
    }

    /**
     * 查询模板角色
     * @param singleCriterionRequest request
     * @return RestResponse
     */
    @Override
    public RestResponse<?> retrieveRoles(SingleCriterionRequest singleCriterionRequest) {
        //获取request中的模板id
        Long id = Long.valueOf(singleCriterionRequest.getCriterion());
        //根据模板id获取模板编号
        String templateSerialNo = this.templateRepository.getSerialNoById(id);
        //获取request中的isDeleted
        Integer isDeleted = (null == singleCriterionRequest.getIsDeleted()) ? MdvnConstant.ZERO : singleCriterionRequest.getIsDeleted();
        //根据id查询模板的角色信息
        List<Object[]> resultSet = this.roleRepository.findRoleTerseInfoByHostSerial(templateSerialNo, isDeleted);
        //转换结果集并返回
        return RestResponseUtil.success(ConvertObjectUtil.convertObjectArray2TerseInfo(resultSet));
    }

    /**
     * 构建template编号
     *
     * @return 编号
     */
    private String buildSerialNo() {
        //查询表中的最大id  maxId
        Long maxId = this.roleRepository.getMaxId();
        //如果表中没有数据，则给maxId赋值为0
        if (maxId == null) {
            maxId = Long.valueOf(MdvnConstant.ZERO);
        }
        maxId += MdvnConstant.ONE;
        return MdvnConstant.TR + maxId;
    }
}
