package com.mdvns.mdvn.template.repository;

import com.mdvns.mdvn.template.domain.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    //查询id的最大值
    @Query(value = "select max(id) from Delivery", nativeQuery = true)
    Long getMaxId();

    //查询指定id集合的terseInfo
    @Query("select d.id, d.serialNo, d.name from Delivery d where d.id in ?1")
    List<Object[]> findTerseInfoById(List<Long> ids);

    //根据hostSerialNo和isDeleted查询
    List<Delivery> findByHostSerialNoAndIsDeleted(String hostSerialNo, Integer isDeleted);

}
