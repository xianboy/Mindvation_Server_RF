package com.mdvns.mdvn.mdvnfile.repository;


import com.mdvns.mdvn.mdvnfile.domain.entity.AttchInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttchInfoRepository extends JpaRepository<AttchInfo, Integer> {


    List<AttchInfo> findByIdIn(List<Integer> idList);
}
