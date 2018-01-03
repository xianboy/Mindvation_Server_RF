package com.mdvns.mdvn.task.repository;

import com.mdvns.mdvn.task.domain.entity.TaskHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<TaskHistory, Long> {


    //查询指定id的task的历史记录:支持分页
    Page<TaskHistory> findByTaskId(Long taskId, Pageable pageable);

}
