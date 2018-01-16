package com.mdvns.mdvn.template.repository;

import com.mdvns.mdvn.common.bean.model.TerseInfo;
import com.mdvns.mdvn.template.domain.entity.FunctionLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelRepository extends JpaRepository<FunctionLabel, Long> {
    //查询id的最大值
    @Query(value = "select max(id) from FunctionLabel")
    Long getMaxId();

    //根据name查询
    FunctionLabel findByName(String name);

    //查询指定id集合的terseInfo
    @Query("select f.id, f.serialNo, f.name from FunctionLabel f where f.id in ?1")
    List<Object[]> findTerseInfoById(List<Long> ids);

    //根据name和hostSerialNo查询
    List<FunctionLabel> findByHostSerialNoAndIsDeleted(String hostSerialNo, Integer isDeleted);

    //查询指定hostSerialNo 和 isDeleted属性的 id
    @Query("select f.id from FunctionLabel f where f.hostSerialNo=?1 and f.isDeleted=?2")
    List<Long> findIdByHostSerialNoAndIsDeleted(String hostSerialNo, Integer isDeleted);

    //获取指定hostSerialNo的过程的name
    @Query("select f.name from FunctionLabel f where f.hostSerialNo=?1 and f.isDeleted=?2")
    List<String> findSubLabel(String serialNo, Integer isDeleted);

    //查询指定hostSerialNo的过程方法TerseInfo
    @Query("select f.id, f.serialNo, f.name from FunctionLabel f where f.hostSerialNo=?1 and f.isDeleted=?2")
    List<Object[]> findTerseInfoByHostSerialNo(String hostSerialNo, Integer isDeleted);

    //查询id为指定id集合中的functionLabel
    @Query("select f.id, f.serialNo, f.name from FunctionLabel f where f.id in ?1")
    List<Object[]> findTerseInfoByIdList(List<Long> subLabel);
}
