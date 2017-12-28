package com.mdvns.mdvn.websocket.domain;


import com.mdvns.mdvn.common.bean.model.Staff;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@Data
@NoArgsConstructor
public class ServerPushInfo {

    private Integer uuId;
    private String subjectType;
    private String subjectId;
    private String type;
    private Integer oldProgress;
    private Integer newProgress;
    private String taskByStoryId;
    private Long createTime;
    private Long initiatorId;//发起人
    private Long recipientId;//接收人
    private Staff initiator;
}
