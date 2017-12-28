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

}
