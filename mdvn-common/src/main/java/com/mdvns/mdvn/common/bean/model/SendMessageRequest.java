package com.mdvns.mdvn.common.bean.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@NoArgsConstructor
public class SendMessageRequest {
    private ServerPush serverPushResponse;
    private List<Long> staffIds;
    private Long initiatorId;//发起人
}
