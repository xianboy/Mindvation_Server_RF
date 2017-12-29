package com.mdvns.mdvn.mdvnfile.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.net.InetAddress;
import java.net.UnknownHostException;


@Configuration
public class LocalHostUtil implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(LocalHostUtil.class);

    private static EmbeddedServletContainerInitializedEvent event;

    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        LocalHostUtil.event = event;
    }

    public static Integer getPort() {
        int port = event.getEmbeddedServletContainer().getPort();
        Assert.state(port != -1, "端口号获取失败!");
        LOG.info("端口号为：{}", port);
        return port;
    }

    public static String getIp() throws UnknownHostException {
        String ip = InetAddress.getLocalHost().getHostAddress();
        LOG.info("本机IP为：{}", ip);
        return ip;
    }


}
