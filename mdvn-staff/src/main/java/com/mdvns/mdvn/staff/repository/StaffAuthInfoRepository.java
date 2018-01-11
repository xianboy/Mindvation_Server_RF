package com.mdvns.mdvn.staff.repository;

import com.mdvns.mdvn.staff.domain.entity.StaffAuthInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffAuthInfoRepository extends JpaRepository<StaffAuthInfo, Integer> {

//    List<StaffAuthInfo> findByProjIdAndHierarchyIdAndStaffId(String projId, String hierarchyId, String staffId);
    List<StaffAuthInfo> findByProjSerialNoAndHostSerialNoAndStaffIdAndIsDeleted(String projSerialNo, String hostSerialNo, Long staffId, Integer isDeletd);
//    int deleteAllByProjIdAndHierarchyId(String projId, String hierarchyId);
//    StaffAuthInfo findFirstByProjIdAndHostIdAndStaffIdAndAuthCode(Long projId, Long hostId, Long staffId, Integer authCode);
    StaffAuthInfo findFirstByProjSerialNoAndHostSerialNoAndStaffIdAndAuthCodeAndIsDeleted(String projSerialNo, String hostSerialNo, Long staffId, Integer authCode, Integer isDeleted);

}
