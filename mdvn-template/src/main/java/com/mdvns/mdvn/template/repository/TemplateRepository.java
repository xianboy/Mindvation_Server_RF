package com.mdvns.mdvn.template.repository;

import com.mdvns.mdvn.template.domain.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TemplateRepository extends JpaRepository<Template, Long> {

    //查询id的最大值
    @Query(value = "select max(id) from Template", nativeQuery = true)
    Long getMaxId();

    //查询指定name的template
    Template findByName(String name);

    //根据industryId查询
    @Query("select t.id, t.serialNo, t.name from Template t where industryId = ?1")
    List<Object[]> findByIndustryId(Long industryId);

    //查询id在集合中的数据
    List<Template> findByIdIn(List<Long> ids);

    //根据id获取编号
    @Query("select t.serialNo from Template t where t.id=?1")
    String getSerialNoById(Long id);

    //获取指定ID的模板名称
    @Query("select t.name from Template t where t.id =?1")
    String findNameById(Long id);
}
