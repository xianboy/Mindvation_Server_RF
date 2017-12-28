package com.mdvns.mdvn.template.service.impl;

import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.template.domain.entity.TemplateRole;
import com.mdvns.mdvn.template.repository.TemplateRoleRepository;
import com.mdvns.mdvn.template.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private TemplateRoleRepository roleRepository;

    /**
     * 创建模板角色
     * @param creatorId creatorId
     * @param hostSerialNo hostSerialNo
     * @param roles roles
     * @return list
     */
    @Override
    public List<TemplateRole> createRoles(Long creatorId, String hostSerialNo, List<String> roles) {
        List<TemplateRole> roleList = new ArrayList<>();
        for (String name : roles) {
            TemplateRole role = new TemplateRole();
            role.setCreatorId(creatorId);
            role.setHostSerialNo(hostSerialNo);
            role.setSerialNo(buildSerialNo());
            role.setName(name);
            roleList.add(role);
        }
        return this.roleRepository.save(roleList);
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
