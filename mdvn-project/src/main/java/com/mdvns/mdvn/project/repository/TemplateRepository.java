package com.mdvns.mdvn.project.repository;

import com.mdvns.mdvn.project.domain.entity.ProjectTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TemplateRepository extends JpaRepository<ProjectTemplate, Long> {
    //查询指定项目的模板id
    @Query("select pt.templateId from ProjectTemplate pt where pt.projId=?1 and pt.isDeleted=?2")
    List<Long> findTemplatesByProjIdAndIsDeleted(Long projId, Integer isDeleted);
    //根据projId和templateId查询
    ProjectTemplate findByProjIdAndTemplateId(Long projId, Long templateId);

    /*update*/
    @Modifying
    @Query("update ProjectTemplate pt set pt.isDeleted = ?1 where pt.projId=?2 and pt.templateId in ?3")
    Integer updateIsDeleted(Integer isDeleted, Long projId, List<Long> templates);
}
