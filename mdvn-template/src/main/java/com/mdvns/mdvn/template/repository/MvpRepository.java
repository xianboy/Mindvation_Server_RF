package com.mdvns.mdvn.template.repository;

import com.mdvns.mdvn.template.domain.entity.MvpTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MvpRepository extends JpaRepository<MvpTemplate, Long> {

    //根据hostSerialNo和isDeleted查询
    List<MvpTemplate> findDistinctByHostSerialNoAndIsDeleted(String hostSerialNo, Integer isDeleted);
}
