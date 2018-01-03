package com.mdvns.mdvn.project.repository;

import com.mdvns.mdvn.project.domain.entity.ProjectStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LeaderRepository extends JpaRepository<ProjectStaff, Long> {
    //根据projId和staffId查询
    ProjectStaff findByProjIdAndStaffId(Long projId, Long staffId);

    //根据projId查询对应的staffId
    @Query("select ps.staffId from ProjectStaff ps where ps.projId = ?1 and ps.isDeleted = ?2")
    List<Long> findLeadersByProjIdAndIsDeleted(Long projId, Integer isDeleted);

    /*update*/
    @Modifying
    @Query("update ProjectStaff ps set ps.isDeleted = ?1 where ps.projId=?2 and ps.staffId in ?3")
    Integer updateIsDeleted(Integer isDeleted, Long projId, List<Long> leaders);

    List<ProjectStaff> findByProjIdAndIsDeleted(Long projId, Integer isDeleted);
}
