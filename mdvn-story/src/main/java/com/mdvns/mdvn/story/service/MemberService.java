package com.mdvns.mdvn.story.service;

import com.mdvns.mdvn.common.bean.MemberRequest;
import com.mdvns.mdvn.common.bean.model.RoleMember;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.story.domain.entity.Story;

import java.util.List;

public interface MemberService {
    Integer buildMembers(Long creatorId, Long id, List<MemberRequest> members);

    //获取指定id的story的成员
    List<RoleMember> getRoleMembers(Long staffId, Long storyId, Long templateId, Integer isDeleted) throws BusinessException;

    //修改角色成员
    void updateRoleMembers(Long staffId, Long requirementId, List<MemberRequest> roleMembers);

    List<Long> getStoryMembers(Long staffId, Story story) throws BusinessException;
}
