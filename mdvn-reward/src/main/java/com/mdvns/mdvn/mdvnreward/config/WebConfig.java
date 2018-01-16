package com.mdvns.mdvn.mdvnreward.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "url")
@Component
@Data
public class WebConfig {
    private String rtrvStaffInfoByIdUrl;
    private String retrieveAllStaffUrl;
    private String findTagInfoByIdUrl;
    private String retrieveHotTagListUrl;
}
