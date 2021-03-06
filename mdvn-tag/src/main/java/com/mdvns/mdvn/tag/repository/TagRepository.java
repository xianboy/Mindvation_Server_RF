package com.mdvns.mdvn.tag.repository;

import com.mdvns.mdvn.tag.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    //查询指定名称的tag
    Tag findByName(String name);

    List<Tag> findByNameContains(String name);

//    Page<Tag> findByNameContains(String name, Pageable pageable);

    //查询id的最大值
    @Query(value = "select max(id) from tag", nativeQuery = true)
    Long getMaxId();

    //获取指定id集合的id和name
    @Query("select t.id, t.serialNo, t.name from Tag t where t.id in ?1 order by t.id")
    List<Object[]> findTerseInfoById(List<Long> ids);

    @Query(value="SELECT * FROM tag WHERE id IN (SELECT tag_id FROM reward WHERE DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= DATE(create_time))", nativeQuery = true)
    List<Tag> findHotTagListInfo();

    /*一周内创建悬赏的标签list*/
    @Query(value="SELECT * FROM tag WHERE id IN (SELECT tag_id FROM reward WHERE DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= DATE(create_time)) limit ?1,?2", nativeQuery = true)
    List<Tag> findRewardHotTagsHavePageable(Integer m,Integer n);

    /*一周内创建求助的标签list*/
    @Query(value="SELECT * FROM tag WHERE id IN (SELECT tag_id FROM issue WHERE DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= DATE(create_time)) limit ?1,?2", nativeQuery = true)
    List<Tag> findIssueHotTagsHavePageable(Integer m,Integer n);

}
