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

    /*查询揭榜过悬赏的所有员工*/
    @Query(value="SELECT DISTINCT unveil_id FROM reward WHERE is_unveil = 1", nativeQuery = true)
    List<Long> findAllRewardStaffList();

    /*查询每个员工已解决的reward*/
    @Query(value="SELECT * FROM reward WHERE is_resolved = 1 AND unveil_id = ?1", nativeQuery = true)
    List<Reward> findAllRewardListHaveResolved(Long creatorId);

    /*查询每个员工揭榜过的reward*/
    @Query(value="SELECT * FROM reward WHERE is_unveil = 1 AND unveil_id = ?1", nativeQuery = true)
    List<Reward> findAllRewardListHaveUnveil(Long creatorId);
}
