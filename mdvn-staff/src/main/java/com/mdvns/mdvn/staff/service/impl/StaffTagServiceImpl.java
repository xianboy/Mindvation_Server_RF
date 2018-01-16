package com.mdvns.mdvn.staff.service.impl;

import com.mdvns.mdvn.common.bean.model.AddOrRemoveById;
import com.mdvns.mdvn.common.bean.model.Tag;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.common.exception.BusinessException;
import com.mdvns.mdvn.common.exception.ErrorEnum;
import com.mdvns.mdvn.common.util.MdvnCommonUtil;
import com.mdvns.mdvn.common.util.RestTemplateUtil;
import com.mdvns.mdvn.staff.config.WebConfig;
import com.mdvns.mdvn.staff.domain.entity.StaffTag;
import com.mdvns.mdvn.staff.repository.StaffTagRepository;
import com.mdvns.mdvn.staff.service.StaffTagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class StaffTagServiceImpl implements StaffTagService {
    private static final Logger LOG = LoggerFactory.getLogger(StaffTagServiceImpl.class);

    @Autowired
    private StaffTagRepository staffTagRepository;

    @Resource
    private WebConfig webConfig;

    @Override
    @Transactional
    public List<StaffTag> createStaffTag(Long staffId, List<Long> tags, Long creatorId) {
        LOG.info("id为：[{}]的staff，准备创建id为：[{}] 的staff的标签id为：[{}]的映射信息.", creatorId, staffId, tags.toString());
        List<StaffTag> staffTags = new ArrayList<>();
        //遍历tags构建StaffTag
        for (Long tagId : tags) {
            StaffTag staffTag = new StaffTag();
            staffTag.setCreatorId(staffId);
            staffTag.setStaffId(creatorId);
            staffTag.setTagId(tagId);
            staffTag.setCreateTime(new Timestamp(System.currentTimeMillis()));
            staffTag.setIsDeleted(MdvnConstant.ZERO);
            staffTag = this.staffTagRepository.saveAndFlush(staffTag);
            staffTags.add(staffTag);
        }
        LOG.info("保存映射信息完成");
        return staffTags;
    }

    /**
     * 移除标签映射
     *
     * @param staffId    当前用户id
     * @param employeeId 要更改的员工id
     * @param tags       需要移除的标签id
     * @return number
     */
    @Override
    @Modifying
    public Integer updateIsDeleted(Long staffId, Long employeeId, List<Long> tags, Integer isDeleted) {
        LOG.info("id为：[{}]的staff，准备去掉id为：[{}] 的员工的id为：[{}]的标签映射信息.", staffId, employeeId, tags.toString());
        return this.staffTagRepository.updateIsDeleted(isDeleted, employeeId, tags);
    }

    /**
     * 更新员工标签映射
     *
     * @param staffId    当前用户id
     * @param employeeId 要更改的员工id
     * @param tags       需要更新的标签id
     */
    @Override
    public void updateTag(Long staffId, Long employeeId, AddOrRemoveById tags) throws BusinessException {
        //删除标签映射
        if (null != tags.getRemoveList()) {
            MdvnCommonUtil.emptyList(tags.getRemoveList(), ErrorEnum.ILLEGAL_ARG, "删除标签不能为空");
            updateIsDeleted(staffId, employeeId, tags.getRemoveList(), MdvnConstant.ONE);
        }
        //添加新增标签映射
        if (null != tags.getAddList()) {
            MdvnCommonUtil.emptyList(tags.getAddList(), ErrorEnum.ILLEGAL_ARG, "新增标签不能为空");
            List<Long> addTags = new ArrayList<>();
            List<Long> updateTags = new ArrayList<>();
            for (Long id : tags.getAddList()) {
                //如果标签映射不存在添加id到addTags,已存在则添加id到removeTags,
                StaffTag st = this.staffTagRepository.findByStaffIdAndTagId(employeeId, id);
                if (null == st) {
                    addTags.add(id);
                } else {
                    updateTags.add(id);
                }
            }
            //更新已存在映射的isDeleted为0
            if (updateTags.size() > 0) {
                updateIsDeleted(staffId, employeeId, updateTags, MdvnConstant.ZERO);
            }
            //添加新映射
            if (addTags.size() > 0) {
                createStaffTag(staffId, addTags, employeeId);
            }

        }
    }

    /**
     * 获取某个员工的便签对象信息
     *
     * @param id
     * @return
     */
    @Override
    public List<Tag> getStaffTagInfo(Long id) throws BusinessException {
        List<StaffTag> staffTags = this.staffTagRepository.findByStaffIdAndIsDeleted(id, 0);
        if (staffTags.size() == 0) {
            return new ArrayList<>();
        }
        List<Long> tagIds = new ArrayList<>();
        for (int i = 0; i < staffTags.size(); i++) {
            StaffTag staffTag = staffTags.get(i);
            Long tagId = staffTag.getTagId();
            if (!tagIds.isEmpty() && tagIds.contains(tagId)) {
                continue;
            }
            tagIds.add(tagId);
        }
        //构建获取指定项目标签url
        String retrieveTagInfosUrl = webConfig.getRetrieveTagInfosUrl();
        //调用tag模块获取标签信息
        List<Tag> tagList = RestTemplateUtil.retrieveTagInfos(id, tagIds, retrieveTagInfosUrl);
        return tagList;
    }
}
