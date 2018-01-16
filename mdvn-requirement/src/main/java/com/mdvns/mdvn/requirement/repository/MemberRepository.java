package com.mdvns.mdvn.requirement.repository;

import com.mdvns.mdvn.requirement.domain.entity.RequirementMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<RequirementMember, Long> {
    //根据requirementId 查询 roleId
    @Query("select distinct(rm.roleId) from RequirementMember rm where rm.requirementId=?1 and rm.isDeleted=?2")
    List<Long> findRoleIdByRequirementIdAndIsDeleted(Long requirementId, Integer isDeleted);

    //查询指定requirement中指定角色的成员
    @Query("select rm.memberId from RequirementMember rm where rm.requirementId=?1 and rm.roleId=?2 and rm.isDeleted=?3  ")
    List<Long> findMemberIdByRoleIdAndRequirementIdAndIsDeleted(Long requirementId, Long roleId, Integer isDelete);

    //修改成员映射
    @Modifying
    @Query("update RequirementMember rm set rm.isDeleted=?4 where rm.requirementId=?1 and rm.roleId=?2 and rm.memberId in ?3")
    void updateIsDeleted(Long requirementId, Long roleId, List<Long> removeList, Integer isDeleted);

    //根据requirementId、roleId、memberId查数据
    RequirementMember findByRequirementIdAndRoleIdAndMemberId(Long requirementId, Long roleId, Long memberId);
}
