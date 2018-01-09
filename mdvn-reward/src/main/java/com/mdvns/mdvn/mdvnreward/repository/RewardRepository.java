package com.mdvns.mdvn.mdvnreward.repository;


import com.mdvns.mdvn.common.bean.model.Tag;
import com.mdvns.mdvn.mdvnreward.domain.entity.Reward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {

    Reward findBySerialNo(String serialNo);

    Page<Reward> findByIsUnveilAndIsDeleted(Pageable pageable, Integer isUnveil,Integer isDeleted);

    @Query(value="SELECT * FROM tag WHERE id IN (SELECT tag_id FROM reward WHERE DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= DATE(create_time))", nativeQuery = true)
    Page<Tag> findTagListInfo(Pageable pageable);
}
