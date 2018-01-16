package com.mdvns.mdvn.websocket.web;


import com.mdvns.mdvn.common.bean.RestResponse;
import com.mdvns.mdvn.common.bean.model.SendMessageRequest;
import com.mdvns.mdvn.websocket.domain.RtrvServerPushListRequest;
import com.mdvns.mdvn.websocket.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping(value = {"/websocket", "/v1.0/websocket"})
public class WebSocketController {

    @Autowired
    private WebSocketService webSocketService;


    /**
     * 推送信息
     * @param request
     * @return
     */
    @PostMapping(value = "/sendMessage")
    public Boolean sendMessage(@RequestBody SendMessageRequest request) throws IOException {
        return this.webSocketService.sendMessage(request);
    }

    /**
     * 推送信息（给自己定时推送消息）
     * @param request
     * @return
     */
    @PostMapping(value = "/sendMessageToOneself")
    public Boolean sendMessageToOneself(@RequestBody SendMessageRequest request) throws IOException {
        return this.webSocketService.sendMessageToOneself(request);
    }

    /**
     * 获取不在线时的推送信息
     * @param request
     * @return
     */
    @PostMapping(value = "/rtrvServerPushInfoList")
    public RestResponse<?> rtrvServerPushInfoList(@RequestBody RtrvServerPushListRequest request) throws IOException {
        return this.webSocketService.rtrvServerPushInfoList(request);
    }


    /**
     * 删除推送信息
     * @param uuId
     * @return
     */
    @PostMapping(value = "/deleteServerPushInfo/{uuId}")
    public RestResponse<?> deleteServerPushInfo(@PathVariable Integer uuId) throws IOException {
        return this.webSocketService.deleteServerPushInfo(uuId);
    }


}
