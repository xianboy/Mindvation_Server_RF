package com.mdvns.mdvn.task.repository;

import com.mdvns.mdvn.task.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    /*query*/
    @Query(value = "select max(id) from Task", nativeQuery = true)
    Long getMaxId();

    //更新进度和备注
    @Modifying
    @Query("update Task t set t.progress = ?1, t.comment = ?2 where t.id = ?3 ")
    void updateProgress(Integer progress, String comment, Long hostId);

    //更新进度
    @Modifying
    @Query("update Task t set t.progress = ?1 where t.id = ?2 ")
    void updateProgress(Integer progress, Long hostId);

    //获取指定hostSerialNo的task的id
    @Query("select t.id from Task t where t.hostSerialNo=?1 and t.isDeleted=?2")
    List<Long> findIdByHostSerialNoAndIsDeleted(String hostSerialNo, Integer isDeleted);

}
