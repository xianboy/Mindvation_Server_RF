package com.mdvns.mdvn.websocket.domain.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Component
@Data
@NoArgsConstructor
public class ServerPush {

    @Id
    @GeneratedValue
    private Integer uuId;
    private String subjectType;//project、requirement、story、task、comment
    private String subjectId;
    private String type;//create、update、@
    private Integer oldProgress;//以前的进度
    private Integer newProgress;//现在的进度
    private String taskByStoryId;//task所属的storyId
    private Timestamp createTime;
    private Long initiatorId;//发起人
    private Long recipientId;//接收人
}
