package com.mdvns.mdvn.issue.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "url")
@Component
@Data
public class WebConfig {
    private String rtrvStaffInfoByIdUrl;
    private String findTagInfoByIdUrl;
    private String rtrvCommentInfosUrl;
    private String rtrvCreatorIdUrl;

    private String retrieveStoryMembersUrl;
    private String retrieveReqMembersUrl;
}
