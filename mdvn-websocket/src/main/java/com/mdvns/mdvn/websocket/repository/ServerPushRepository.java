package com.mdvns.mdvn.websocket.repository;


import com.mdvns.mdvn.websocket.domain.entity.ServerPush;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServerPushRepository extends JpaRepository<ServerPush, Integer> {

    List<ServerPush> findByRecipientId(String recipientId);

    //按staffId查询(先按状态排序，再按时间排序，最后按优先级排序)
    @Query(value="SELECT * FROM server_push WHERE recipient_id = ?1 LIMIT ?2,?3", nativeQuery = true)
    List<ServerPush> findByRecipientId(String staffId, Integer m, Integer n);
}
