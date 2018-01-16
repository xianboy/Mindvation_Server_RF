package com.mdvns.mdvn.story.repository;

import com.mdvns.mdvn.story.domain.entity.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {
    /*query*/
    @Query(value = "select max(id) from Story", nativeQuery = true)
    Long getMaxId();

    //根据hostSerialNo查询Story
//    Page<Story> findByHostSerialNo(String hostSerialNo, Pageable pageRequest);
    Page<Story> findByHostSerialNoAndIsDeleted(String hostSerialNo, Integer isDeleted, Pageable pageRequest);
    //根据serialNo查询story
    Story findBySerialNo(String serialNo);

    //更新状态
    @Modifying
    @Query("update Story s set s.status = ?1 where s.id = ?2")
    void updateStatus(String status, Long hostId);

    //修改描述
    @Modifying
    @Query("update Story s set s.description = :description where s.id = :id")
    Integer updateDescription(@Param("description") String description, @Param("id") Long id);

    //修改概要
    @Modifying
    @Query("update Story s set s.summary = :summary where s.id = :id")
    Integer updateSummary(@Param("summary") String summary, @Param("id") Long id);

    //修改描述和概要
    @Modifying
    @Query("update Story s set s.summary = ?1, s.description = ?2 where s.id = ?3")
    Integer updateBoth(String summary, String description, Long id);

    //更新指定hostSerialNo的Story的mvpId
    @Modifying
    @Query("update Story s set s.mvpId=?1 where s.hostSerialNo in ?2")
    Integer updateMvp(Long mvpId, List<String> hostSerialNoList);

    //更新指定Story集合的mvpId
    @Modifying
    @Query("update Story s set s.mvpId=?1 where s.id in ?2")
    void updateMvpIdByIdIn(Long mvpId, List<Long> stories);

    //根据mvpId和isDeleted查询
    List<Story> findByMvpIdAndIsDeleted(Long mvpId, Integer isDeleted);

    //获取hostSerialNo在指定集合中且mvpId为空的Story集合
    List<Story> findByHostSerialNoInAndIsDeletedAndMvpIdIsNull(List<String> hostSerialNoList, Integer isDeleted);

    //获取hostSerialNo在指定集合中指定mvpId的Story集合
    List<Story> findByHostSerialNoInAndMvpIdAndIsDeleted(List<String> hostSerialNoList, Long mvpId, Integer isDeleted);
}
