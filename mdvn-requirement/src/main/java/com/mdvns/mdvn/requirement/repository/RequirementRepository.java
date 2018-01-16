package com.mdvns.mdvn.requirement.repository;

import com.mdvns.mdvn.requirement.domain.entity.Requirement;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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

    //根据编号获取functionLabelId
    @Query("select r.functionLabelId from Requirement r where r.serialNo=?1 and r.isDeleted=?2")
    Long findLabelIdBySerialNoAndIsDeleted(String serialNo, Integer isDeleted);

    //获取指定id的过程方法对应的需求编号
    @Query("select r.serialNo from Requirement r where r.hostSerialNo=?1 and r.functionLabelId in ?2 and r.isDeleted=?3")
    List<String> findSerialNoByHostSerialNoAndFunctionLabelIdInAndIsDeleted(String hostSerialNo, List<Long> labels, Integer isDeleted);

    //获取指定hostSerialNo(项目编号)和templateId的需求编号
    @Query("select r.serialNo from Requirement r where r.hostSerialNo=?1 and r.templateId=?2 and r.isDeleted=?3")
    List<String> findSerialNoByHostSerialNoAndTemplateIdAndIsDeleted(String hostSerialNo, Long templateId, Integer isDeleted);

    //更新指定集合的需求的mvpId(三层)
    @Modifying
    @Query("update Requirement r set r.mvpId = ?1 where r.serialNo in ?2")
    void updateMvpIdBySerialNoIn(Long mvpId, List<String> requirements);

    //修改指定id集合的需求的mvpId
    @Modifying
    @Query("update Requirement r set r.mvpId =?1 where r.id in ?2")
    void updateMvpIdByIdIn(Long mvpId, List<Long> contents);


    //获取指定requirement集合中mvpId 为空的数据
    List<Requirement> findBySerialNoInAndIsDeletedAndMvpIdIsNull(List<String> serialNoList, Integer isDeleted);

    //查询指定requirement集合中指定mvpId的数据
    List<Requirement> findBySerialNoInAndIsDeletedAndMvpId(List<String> serialNoList, Integer isDeleted, Long mvpId);
}
