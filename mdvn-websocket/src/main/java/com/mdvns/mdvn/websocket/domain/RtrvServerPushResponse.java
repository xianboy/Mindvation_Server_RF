package com.mdvns.mdvn.websocket.domain;


import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class RtrvServerPushResponse {

    private List<ServerPushInfo> serverPushes;

    private Integer totalNumbers;

    public List<ServerPushInfo> getServerPushes() {
        return serverPushes;
    }

    public void setServerPushes(List<ServerPushInfo> serverPushes) {
        this.serverPushes = serverPushes;
    }

    public Integer getTotalNumbers() {
        return totalNumbers;
    }

    public void setTotalNumbers(Integer totalNumbers) {
        this.totalNumbers = totalNumbers;
    }
}
