package com.mdvns.mdvn.template.repository;

import com.mdvns.mdvn.template.domain.entity.MvpTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItTemplateRepository extends JpaRepository<MvpTemplate, Long> {
}
