package com.mdvns.mdvn.dashboard.repository;

import com.mdvns.mdvn.dashboard.domain.entity.MvpTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MvpRepository extends JpaRepository<MvpTemplate, Long> {



    List<MvpTemplate> findTop2ByHostSerialNoAndTemplateIdAndStatusIsNotOrderByMvpIndexAsc(String hostSerialNo, Long templateId, String status);
}
