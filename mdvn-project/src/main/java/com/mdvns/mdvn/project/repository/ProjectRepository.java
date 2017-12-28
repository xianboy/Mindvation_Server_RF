package com.mdvns.mdvn.project.repository;

import com.mdvns.mdvn.project.domain.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Long>{
    /*query*/
    @Query(value = "select max(id) from project", nativeQuery = true)
    Long getMaxId();

    //按名称查询project
    Project findByName(String name);


    /*update*/

    //更新状态
    @Modifying
    @Query("update Project p set p.status = ?1 where p.id = ?2")
    void updateStatus(String status, Long id);

    //更新描述
    @Modifying
    @Query("update Project p set p.description = :description where p.id = :id")
    Integer updateDesc(@Param("description")String desc, @Param("id")Long projId);

    //更新name
    @Modifying
    @Query("update Project p set p.name = :name where p.id = :id")
    Integer updateName(@Param("name")String name, @Param("id")Long projId);

    //更新name 和 desc
    @Modifying
    @Query("update Project p set p.name = ?1, p.status = ?2 where p.id = ?3")
    Integer updateBasicInfo(String name, String desc, Long projId);

    //根据编号查询
    Project findBySerialNo(String serialNo);
}
