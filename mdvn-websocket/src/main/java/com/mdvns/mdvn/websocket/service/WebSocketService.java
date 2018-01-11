package com.mdvns.mdvn.websocket.service;



import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.model.SendMessageRequest;
import com.mdvns.mdvn.websocket.domain.RtrvServerPushListRequest;
import java.io.IOException;

public interface WebSocketService {
    Boolean sendMessage(SendMessageRequest request) throws IOException;

    Boolean sendMessageToOneself(SendMessageRequest request) throws IOException;

    RestResponse<?> rtrvServerPushInfoList(RtrvServerPushListRequest request);

    RestResponse<?> deleteServerPushInfo(Integer uuId);
}
