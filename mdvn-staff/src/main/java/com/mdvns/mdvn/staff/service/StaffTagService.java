package com.mdvns.mdvn.staff.service;

import com.mdvns.mdvn.staff.domain.entity.StaffTag;

import java.util.List;

public interface StaffTagService {
    //用户标签信息
    List<StaffTag> createStaffTag(Long id, List<Long> tags, Long creatorId);
}
