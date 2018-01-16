package com.mdvns.mdvn.staff.service.impl;

import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.staff.domain.entity.StaffTag;
import com.mdvns.mdvn.staff.repository.StaffTagRepository;
import com.mdvns.mdvn.staff.service.StaffTagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class StaffTagServiceImpl implements StaffTagService {
    private static final Logger LOG = LoggerFactory.getLogger(StaffTagServiceImpl.class);

    @Autowired
    private StaffTagRepository staffTagRepository;

    @Override
    @Transactional
    public List<StaffTag> createStaffTag(Long staffId, List<Long> tags, Long creatorId) {
        LOG.info("id为：[{}]的staff，准备创建id为：[{}] 的staff的标签id为：[{}]的映射信息.", creatorId, staffId, tags.toString());
        List<StaffTag> staffTags = new ArrayList<>();
        //遍历tags构建StaffTag
        for (Long tagId : tags) {
            StaffTag staffTag = new StaffTag();
            staffTag.setCreatorId(creatorId);
            staffTag.setStaffId(staffId);
            staffTag.setTagId(tagId);
            staffTag.setCreateTime(new Timestamp(System.currentTimeMillis()));
            staffTag.setIsDeleted(MdvnConstant.ZERO);
            staffTag = this.staffTagRepository.saveAndFlush(staffTag);
            staffTags.add(staffTag);
        }
        LOG.info("保存映射信息完成");
        return staffTags;
    }
}
