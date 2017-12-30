package com.mdvns.mdvn.mdvnfile.repository;


import com.mdvns.mdvn.mdvnfile.domain.entity.AttchInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttchInfoRepository extends JpaRepository<AttchInfo, Long> {

    List<AttchInfo> findBySubjectIdAndIsDeleted(String subjectId,Integer isDeleted);
    List<AttchInfo> findByIdIn(List<Long> idList);
}
