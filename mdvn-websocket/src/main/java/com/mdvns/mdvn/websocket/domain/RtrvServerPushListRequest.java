package com.mdvns.mdvn.websocket.domain;


import org.springframework.stereotype.Component;

@Component
public class RtrvServerPushListRequest {

    private String recipientId;

    private Integer startNum;

    private Integer size;

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public Integer getStartNum() {
        return startNum;
    }

    public void setStartNum(Integer startNum) {
        this.startNum = startNum;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
