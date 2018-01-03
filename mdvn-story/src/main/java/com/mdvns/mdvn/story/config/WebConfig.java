package com.mdvns.mdvn.story.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "url")
@Data
@Component
public class WebConfig {

    //自定义过程方法url
    private String customLabelUrl;

    //根据name和hostSerialNo查询FunctionLabel的url
    private String retrieveLabelByNameAndHostUrl;

    //获取标签url
    private String retrieveTagsUrl;

    //获取成员url
    private String retrieveMembersUrl;

    //获取过程方法url
    private String retrieveLabelUrl;

    //获取TemplateRole的url
    private String retrieveRolesUrl;

    //获取上层模块的角色成员Url
    private String retrieveOptionalMemberUrl;

    //获取上层模块过程方法id的Url
    private String retrieveHostLabelIdUrl;

    //获取指定id的过程方法及其子过程过程方法
    private String retrieveLabelDetailUrl;

    //获取Story的task
    private String retrieveTaskListUrl;
}
