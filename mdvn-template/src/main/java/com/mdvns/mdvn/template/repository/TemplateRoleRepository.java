package com.mdvns.mdvn.template.repository;

import com.mdvns.mdvn.template.domain.entity.TemplateRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateRoleRepository extends JpaRepository<TemplateRole, Long> {
    //查询id的最大值
    @Query(value = "select max(id) from template_role", nativeQuery = true)
    Long getMaxId();

    //查询指定id集合的terseInfo
    @Query("select t.id, t.serialNo, t.name from TemplateRole t where t.id in ?1")
    List<Object[]> findTerseInfoById(List<Long> ids);

    //查询指定模板的所有有色的terseInfo
    @Query("select tr.id, tr.serialNo, tr.name from TemplateRole tr where tr.hostSerialNo=?1 and tr.isDeleted=?2")
    List<Object[]> findRoleTerseInfoByHostSerial(String hostSerialNo, Integer isDeleted);

    //根据hostSerialNo查询
    List<TemplateRole> findDistinctByHostSerialNoAndIsDeleted(String hostSerialNo, Integer isDeleted);


}
