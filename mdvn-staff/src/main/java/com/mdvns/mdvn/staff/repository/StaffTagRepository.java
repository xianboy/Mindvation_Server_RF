package com.mdvns.mdvn.staff.repository;

import com.mdvns.mdvn.staff.domain.entity.StaffTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StaffTagRepository extends JpaRepository<StaffTag, Long> {

    List<StaffTag> findByStaffIdAndIsDeleted(Long id, Integer idDeleted);

    List<StaffTag> findByTagIdIn(List<String> tags);

    @Query("select st.tagId from StaffTag st where st.staffId =?1")
    List<String> findTagIdByStaffId(String staffId);

    //查询tagID在集合中的数据
    List<StaffTag> findByIsDeletedAndTagIdIn(Integer isDeleted, List<Long> tags);

    //根据projId和tagId查询
    StaffTag findByStaffIdAndTagId(Long staffId, Long tagId);

    /*update*/
    @Modifying
    @Query("update StaffTag st set st.isDeleted = ?1 where st.staffId=?2 and st.tagId in ?3")
    Integer updateIsDeleted(Integer isDeleted, Long staffId, List<Long> tags);


}
