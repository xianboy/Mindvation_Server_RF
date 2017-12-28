package com.mdvns.mdvn.template.service;

import com.mdvns.mdvn.template.domain.entity.TemplateRole;

import java.util.List;

public interface RoleService {
    //创建模板角色
    List<TemplateRole> createRoles(Long creatorId, String hostSerialNo, List<String> roles);
}
