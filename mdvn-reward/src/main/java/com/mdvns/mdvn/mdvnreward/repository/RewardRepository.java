package com.mdvns.mdvn.mdvnreward.repository;

import com.mdvns.mdvn.mdvnreward.domain.entity.Reward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {

    Reward findBySerialNo(String serialNo);

    /*已揭榜的*/
    Page<Reward> findByIsUnveilAndIsDeleted(Pageable pageable, Integer isUnveil,Integer isDeleted);

    /*已解决的*/
    Page<Reward> findByIsResolvedAndIsDeleted(Pageable pageable, Integer isResolved,Integer isDeleted);

    @Query(value="SELECT * FROM tag WHERE id IN (SELECT tag_id FROM reward WHERE DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= DATE(create_time))", nativeQuery = true)
    List findTagListInfo();

    @Query(value="SELECT * FROM tag WHERE id IN (SELECT tag_id FROM reward WHERE DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= DATE(create_time)) limit ?1,?2", nativeQuery = true)
    List findTagsHavePageable(Integer m,Integer n);
}
