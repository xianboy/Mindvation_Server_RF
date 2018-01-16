package com.mdvns.mdvn.template.repository;

import com.mdvns.mdvn.common.bean.model.TerseInfo;
import com.mdvns.mdvn.template.domain.entity.FunctionLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelRepository extends JpaRepository<FunctionLabel, Long> {
    //查询id的最大值
    @Query(value = "select max(id) from FunctionLabel")
    Long getMaxId();

    //查询指定id集合的terseInfo
    @Query("select f.id, f.serialNo, f.name from FunctionLabel f where f.id in ?1")
    List<Object[]> findTerseInfoById(List<Long> ids);

    //根据name和hostSerialNo查询
    List<FunctionLabel> findByHostSerialNoAndIsDeleted(String hostSerialNo, Integer isDeleted);

    //修改MVP
    @Modifying
    @Query("update FunctionLabel f set f.mvpId = ?1 where f.hostSerialNo=?2 and f.name in ?3")
    void addMvp4Label(Long mvpId, String hostSerialNo, List<String> labelNames);

    //根据mvpId和isDeleted查询Id
    @Query("select f.id from FunctionLabel f where f.mvpId=?1 and f.isDeleted=?2")
    List<Long> findIdByMvpIdAndIsDeleted(Long aLong, Integer isDeleted);
}
