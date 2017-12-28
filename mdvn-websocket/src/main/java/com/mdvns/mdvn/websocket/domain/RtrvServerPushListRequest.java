package com.mdvns.mdvn.websocket.domain;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class RtrvServerPushListRequest {

    private Long recipientId;

    private Integer startNum;

    private Integer size;
}
