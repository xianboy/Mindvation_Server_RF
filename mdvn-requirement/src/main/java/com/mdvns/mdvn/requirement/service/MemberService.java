package com.mdvns.mdvn.requirement.service;

import com.mdvns.mdvn.common.bean.MemberRequest;
import com.mdvns.mdvn.common.bean.model.RoleMember;
import com.mdvns.mdvn.common.exception.BusinessException;

import java.util.List;

public interface MemberService {
    //保存按角色保存需求的成员映射
    Integer handleMembers(Long creatorId, Long id, List<MemberRequest> members);

    //获取指定id的需求的成员
    List<RoleMember> getRoleMembers(Long staffId, Long requirementId, Long templateId, Integer isDeleted) throws BusinessException;

    //修改角色成员
    void updateRoleMembers(Long staffId, Long requirementId, List<MemberRequest> roleMembers);
}
