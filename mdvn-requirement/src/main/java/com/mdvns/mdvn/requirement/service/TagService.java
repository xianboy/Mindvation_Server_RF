package com.mdvns.mdvn.requirement.service;

import com.mdvns.mdvn.common.bean.model.AddOrRemoveById;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.requirement.domain.entity.RequirementTag;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface TagService {
    List<RequirementTag> handleTags(Long creatorId, Long reqmntId, List<Long> tags);

    //根据reqmntId查询标签id
    List<Long> getTags(Long reqmntId, Integer isDeleted);

    //修改标签映射
    void updateTags(Long staffId, Long requirementId, AddOrRemoveById tags) throws BusinessException;

    //删除映射
    @Modifying
    Integer updateIsDeleted(Long staffId, Long requirementId, List<Long> tags, Integer isDeleted);
}

