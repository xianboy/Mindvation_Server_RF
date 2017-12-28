package com.mdvns.mdvn.story.repository;

import com.mdvns.mdvn.story.domain.entity.StoryMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<StoryMember, Long> {

    //查询指定storyId的所有roleId
    @Query("select distinct(s.roleId) from StoryMember s where s.storyId = ?1 and s.isDeleted = ?2")
    List<Long> findRoleIdByStoryIdAndIsDeleted(Long storyId, Integer idDeleted);

    //根据storyId和roleId查询memberId
    @Query("select distinct(s.memberId) from StoryMember s where s.storyId = ?1 and s.roleId = ?2 and s.isDeleted = ?3 order by s.roleId")
    List<Long> findMemberIdByStoryIdAndRoleIdAndIsDeleted(Long storyId, Long roleId, Integer isDeleted);

    //修改成员映射
    @Modifying
    @Query("UPDATE StoryMember m set m.isDeleted = ?4 where m.storyId=?1 and m.roleId = ?2 and m.memberId in ?3")
    void updateIsDeleted(Long storyId, Long roleId, List<Long> removeList, Integer isDeleted);

    //根据storyId、roleId、memberId查询
    StoryMember findByStoryIdAndRoleIdAndMemberId(Long storyId, Long roleId, Long memberId);
}
