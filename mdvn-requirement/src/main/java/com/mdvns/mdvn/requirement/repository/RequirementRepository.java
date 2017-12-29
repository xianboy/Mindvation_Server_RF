package com.mdvns.mdvn.requirement.repository;

import com.mdvns.mdvn.requirement.domain.entity.Requirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RequirementRepository extends JpaRepository<Requirement, Long> {

    /*query*/
    @Query(value = "select max(id) from requirement", nativeQuery = true)
    Long getMaxId();

    //根据projId查询：支持分页
    Page<Requirement> findByHostSerialNo(String hostSerialNo, Pageable pageable);

    //跟新状态
    @Modifying
    @Query("update Requirement r set r.status = ?1 where r.id = ?2")
    void updateStatus(String status, Long id);

    //修改描述
    @Modifying
    @Query("update Requirement r set r.description = :description where r.id = :id")
    Integer updateDescription(@Param("description") String description, @Param("id") Long requirementId);

    //修改概要
    @Modifying
    @Query("update Requirement r set r.summary = :summary where r.id = :id")
    Integer updateSummary(@Param("summary") String summary, @Param("id") Long requirementId);

    //修改描述和概要
    @Modifying
    @Query("update Requirement r set r.summary = ?1, r.description = ?2 where r.id = ?3")
    Integer updateBoth(String summary, String desc, Long requirementId);

    //根据编号查询
    Requirement findBySerialNo(String serialNo);
}
