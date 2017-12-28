package com.mdvns.mdvn.requirement.service.impl;

import com.mdvns.mdvn.common.bean.model.AddOrRemoveById;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.requirement.domain.entity.RequirementTag;
import com.mdvns.mdvn.requirement.repository.TagRepository;
import com.mdvns.mdvn.requirement.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Resource
    private TagRepository tagRepository;

    private static final Logger LOG = LoggerFactory.getLogger(TagServiceImpl.class);

    /**
     * 创建需求标签映射
     * @param creatorId staffId
     * @param reqmntId reqmntId
     * @param tags tags
     * @return list
     */
    @Override
    public List<RequirementTag> handleTags(Long creatorId, Long reqmntId, List<Long> tags) {
        LOG.info("添加需求标签映射开始...");
        List<RequirementTag> reqmntTags = new ArrayList<>();
        //遍历tags保存标签映射
        for (Long tagId:tags) {
            RequirementTag reqmntTag = new RequirementTag();
            reqmntTag.setCreatorId(creatorId);
            reqmntTag.setReqmntId(reqmntId);
            reqmntTag.setTagId(tagId);
            reqmntTag = this.tagRepository.saveAndFlush(reqmntTag);
            reqmntTags.add(reqmntTag);
        }
        LOG.info("保存映射信息完成");
        return reqmntTags;
    }

    /**
     * 获取指定需求id的标签
     * @param reqmntId id
     * @return list
     */
    @Override
    public List<Long> getTags(Long reqmntId) {
        return this.tagRepository.findTagsByReqmntId(reqmntId);
    }

    /**
     * 修改标签映射
     * @param staffId staffId
     * @param reqmntId requirementId
     * @param tags tags
     */
    @Override
    public void updateTags(Long staffId, Long reqmntId, AddOrRemoveById tags) {
        //删除标签映射
        if (null != tags.getRemoveList()) {
            updateIsDeleted(staffId, reqmntId, tags.getRemoveList(), MdvnConstant.ONE);
        }
        //添加新增标签映射
        if (null != tags.getAddList()) {
            List<Long> addTags = new ArrayList<>();
            List<Long> updateTags = new ArrayList<>();
            for (Long tagId : tags.getAddList()) {
                //如果标签映射不存在添加id到addTags,已存在则添加id到removeTags
                RequirementTag rt = this.tagRepository.findByReqmntIdAndTagId(reqmntId, tagId);
                if (null == rt) {
                    addTags.add(tagId);
                } else {
                    updateTags.add(tagId);
                }
            }
            //更新已存在映射的isDeleted为0
            updateIsDeleted(staffId, reqmntId, updateTags, MdvnConstant.ZERO);
            //添加新映射
            handleTags(staffId, reqmntId, addTags);
        }
    }

    /**
     * 移除标签映射
     * @param staffId 当前用户id
     * @param requirementId requirementId
     * @param tags 需要移除的标签id
     * @return number
     */
    @Override
    @Modifying
    public Integer updateIsDeleted(Long staffId, Long requirementId, List<Long> tags, Integer isDeleted) {
        LOG.info("id为：[{}]的staff，准备去掉id为：[{}] 的需求，id为：[{}]的标签映射信息.", staffId, requirementId, tags.toString());
        return this.tagRepository.updateIsDeleted(isDeleted, requirementId, tags);
    }

}
