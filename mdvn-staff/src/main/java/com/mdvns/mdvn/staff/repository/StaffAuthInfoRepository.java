package com.mdvns.mdvn.staff.repository;

import com.mdvns.mdvn.staff.domain.entity.StaffAuthInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffAuthInfoRepository extends JpaRepository<StaffAuthInfo, Integer> {

    List<StaffAuthInfo> findByProjIdAndHierarchyIdAndStaffId(String projId, String hierarchyId, String staffId);

    int deleteAllByProjIdAndHierarchyId(String projId, String hierarchyId);
}
