package com.mdvns.mdvn.task.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "url")
@Component
@Data
public class WebConfig {

    //自定义交付件url
    private String customDeliveryUrl;

    //获取创建人信息
    private String retrieveMembersUrl;

    //获取交付件
    private String retrieveDeliveryUrl;

    //根据id获取staff(创建人)
    private String retrieveCreatorUrl;

    //根据id获取附件信息
    private String retrieveAttachUrl;
}


