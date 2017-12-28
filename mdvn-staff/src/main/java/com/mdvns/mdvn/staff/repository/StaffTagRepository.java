package com.mdvns.mdvn.staff.repository;

import com.mdvns.mdvn.staff.domain.entity.StaffTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StaffTagRepository extends JpaRepository<StaffTag, Long> {

    List<StaffTag> findByStaffIdAndIsDeleted(String staffId, Integer idDeleted);

    List<StaffTag> findByTagIdIn(List<String> tags);

    @Query("select st.tagId from StaffTag st where st.staffId =?1")
    List<String> findTagIdByStaffId(String staffId);


}
