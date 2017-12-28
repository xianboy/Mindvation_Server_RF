package com.mdvns.mdvn.story.repository;

import com.mdvns.mdvn.story.domain.entity.StoryTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<StoryTag, Long> {

    //查询指定storyId对应的所有tagId
    @Query("select s.tagId from StoryTag s where s.storyId = ?1 and s.isDeleted = ?2")
    List<Long> findTagIdByStoryIdAndIsDeleted(Long storyId, Integer isDeleted);

    //根据storyId和tagId查询
    StoryTag findByStoryIdAndTagId(Long storyId, Long tagId);

    /*update*/
    @Modifying
    @Query("update StoryTag st set st.isDeleted = ?1 where st.storyId=?2 and st.tagId in ?3")
    Integer updateIsDeleted(Integer isDeleted, Long reqmntId, List<Long> tags);


}
