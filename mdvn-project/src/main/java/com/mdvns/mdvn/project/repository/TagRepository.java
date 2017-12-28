package com.mdvns.mdvn.project.repository;

import com.mdvns.mdvn.project.domain.entity.ProjectTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<ProjectTag, Long> {
    /*query*/
    //查询指定projId的标签id
    @Query("select pt.tagId from ProjectTag pt where pt.projId = ?1 and pt.isDeleted=?2")
    List<Long> findTagsByProjIdAndIsDeleted(Long projId, Integer isDeleted);

    //根据projId和tagId查询
    ProjectTag findByProjIdAndTagId(Long projId, Long tagId);

    /*update*/
    @Modifying
    @Query("update ProjectTag pt set pt.isDeleted = ?1 where pt.projId=?2 and pt.tagId in ?3")
    Integer updateIsDeleted(Integer isDeleted, Long projId, List<Long> tags);

}
