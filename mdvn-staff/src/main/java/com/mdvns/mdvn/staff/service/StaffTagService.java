package com.mdvns.mdvn.staff.service;

import com.mdvns.mdvn.common.bean.model.AddOrRemoveById;
import com.mdvns.mdvn.common.bean.model.Tag;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.staff.domain.entity.StaffTag;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface StaffTagService {
    //用户标签信息
    List<StaffTag> createStaffTag(Long id, List<Long> tags, Long creatorId);

    @Modifying
    Integer updateIsDeleted(Long staffId, Long projId, List<Long> tags, Integer isDeleted);

    void updateTag(Long staffId, Long id, AddOrRemoveById tags) throws BusinessException;

    List<Tag> getStaffTagInfo(Long id) throws BusinessException;
}
