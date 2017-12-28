package com.mdvns.mdvn.websocket.service;



import com.mdvns.mdvn.common.beans.RestResponse;
import com.mdvns.mdvn.common.beans.SendMessageRequest;
import com.mdvns.mdvn.websocket.domain.RtrvServerPushListRequest;
import com.mdvns.mdvn.websocket.domain.RtrvServerPushResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface WebSocketService {
    Boolean sendMessage(SendMessageRequest request) throws IOException;

    RestResponse rtrvServerPushInfoList(RtrvServerPushListRequest request);

    ResponseEntity<?> deleteServerPushInfo(Integer uuId);
}
