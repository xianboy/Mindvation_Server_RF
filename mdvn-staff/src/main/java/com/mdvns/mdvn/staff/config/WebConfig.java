package com.mdvns.mdvn.staff.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "url")
@Component
public class WebConfig {

    //根据id集合查询标签
    private String retrieveTagsUrl;

    private String findTagInfoByIdUrl;

    private String retrieveTagInfosUrl;

}

