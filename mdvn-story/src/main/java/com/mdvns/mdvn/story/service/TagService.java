package com.mdvns.mdvn.story.service;

import com.mdvns.mdvn.common.bean.model.AddOrRemoveById;
import com.mdvns.mdvn.story.domain.entity.StoryTag;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface TagService {

    //新建Story标签映射
    List<StoryTag> buildTags(Long creatorId, Long id, List<Long> tags);

    //获取指定id的story的标签
    List<Long> getTags(Long staffId, Long storyId, Integer isDeleted);

    //修改标签映射
    void updateTags(Long staffId, Long storyId, AddOrRemoveById tags);

    //删除映射
    @Modifying
    Integer updateIsDeleted(Long staffId, Long storyId, List<Long> tags, Integer isDeleted);

}
