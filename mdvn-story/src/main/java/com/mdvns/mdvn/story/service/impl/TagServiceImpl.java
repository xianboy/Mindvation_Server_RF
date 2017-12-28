package com.mdvns.mdvn.story.service.impl;

import com.mdvns.mdvn.common.bean.model.AddOrRemoveById;
import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.story.domain.entity.StoryTag;
import com.mdvns.mdvn.story.repository.TagRepository;
import com.mdvns.mdvn.story.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private static final Logger LOG = LoggerFactory.getLogger(TagServiceImpl.class);

    @Resource
    private TagRepository tagRepository;

    /**
     * 创建需求标签映射
     * @param storyId storyId
     * @param creatorId staffId
     * @param tags tags
     * @return list
     */
    @Override
    public List<StoryTag> buildTags(Long creatorId, Long storyId, List<Long> tags) {
        LOG.info("添加story标签映射开始...");
        List<StoryTag> storyTags = new ArrayList<>();
        //遍历tags保存标签映射
        for (Long tagId:tags) {
            StoryTag storyTag = new StoryTag();
            storyTag.setCreatorId(creatorId);
            storyTag.setStoryId(storyId);
            storyTag.setTagId(tagId);
            storyTag = this.tagRepository.saveAndFlush(storyTag);
            storyTags.add(storyTag);
        }
        LOG.info("保存映射信息完成");
        return storyTags;
    }

    @Override
    public List<Long> getTags(Long staffId, Long storyId, Integer isDeleted) {
        return this.tagRepository.findTagIdByStoryIdAndIsDeleted(storyId, isDeleted);
    }

    /**
     * 修改标签映射
     * @param staffId staffId
     * @param storyId storyId
     * @param tags tags
     */
    @Override
    public void updateTags(Long staffId, Long storyId, AddOrRemoveById tags) {
        //删除标签映射
        if (null != tags.getRemoveList()) {
            updateIsDeleted(staffId, storyId, tags.getRemoveList(), MdvnConstant.ONE);
        }
        //添加新增标签映射
        if (null != tags.getAddList()) {
            List<Long> addTags = new ArrayList<>();
            List<Long> updateTags = new ArrayList<>();
            for (Long tagId : tags.getAddList()) {
                //如果标签映射不存在添加id到addTags,已存在则添加id到removeTags
                StoryTag st = this.tagRepository.findByStoryIdAndTagId(storyId, tagId);
                if (null == st) {
                    addTags.add(tagId);
                } else {
                    updateTags.add(tagId);
                }
            }
            //更新已存在映射的isDeleted为0
            updateIsDeleted(staffId, storyId, updateTags, MdvnConstant.ZERO);
            //添加新映射
            buildTags(staffId, storyId, addTags);
        }
    }

    /**
     * 移除标签映射
     * @param staffId 当前用户id
     * @param storyId 当前项目id
     * @param tags 需要移除的标签id
     * @return number
     */
    @Override
    @Modifying
    public Integer updateIsDeleted(Long staffId, Long storyId, List<Long> tags, Integer isDeleted) {
        LOG.info("id为：[{}]的staff，准备去掉id为：[{}] 的story，id为：[{}]的标签映射信息.", staffId, storyId, tags.toString());
        return this.tagRepository.updateIsDeleted(isDeleted, storyId, tags);
    }

}
