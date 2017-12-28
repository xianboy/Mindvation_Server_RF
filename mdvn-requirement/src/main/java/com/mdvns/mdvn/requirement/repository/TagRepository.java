package com.mdvns.mdvn.requirement.repository;

import com.mdvns.mdvn.requirement.domain.entity.RequirementTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<RequirementTag, Long> {

    /*query*/
    //查询指定reqmntId的标签id
    @Query("select rt.tagId from RequirementTag rt where rt.reqmntId = ?1")
    List<Long> findTagsByReqmntId(Long reqmntId);

    /*update*/
    @Modifying
    @Query("update RequirementTag rt set rt.isDeleted = ?1 where rt.reqmntId=?2 and rt.tagId in ?3")
    Integer updateIsDeleted(Integer isDeleted, Long reqmntId, List<Long> tags);

    //根据reqmntId, tagId 查询 标签映射
    RequirementTag findByReqmntIdAndTagId(Long reqmntId, Long tagId);
}
