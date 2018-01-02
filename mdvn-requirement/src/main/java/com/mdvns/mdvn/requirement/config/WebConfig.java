package com.mdvns.mdvn.requirement.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "url")
@Component
@Data
public class WebConfig {
    //保存自定义过程方法模块url
    private String customLabelUrl;

    //获取staff的id和name的url
    private String retrieveMembersUrl;

    //调用标签模块获取标签的id和name
    private String retrieveTagsUrl;

    //调用template模块获取role的基本信息
    private String retrieveRolesUrl;

    //调用template模块获取FunctionLabel的id和name
    private String retrieveLabelUrl;

    //获取指定编号的requirement下的Story列表
    private String retrieveStoriesUrl;

    //获取指定id的过程方法及其子过程方法
    private String retrieveLabelAndSubLabelUrl;
}
