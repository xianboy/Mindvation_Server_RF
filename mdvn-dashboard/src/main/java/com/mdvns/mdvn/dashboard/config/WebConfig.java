package com.mdvns.mdvn.dashboard.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "url")
@Component
public class WebConfig {

    //获取指定serialNo的模板的迭代计划
    private String retrieveMvpTemplatesUrl;

    //arrange时新建mvp(四层:story)
    private String createStoryMvpUrl;

    //修改mvpId(三层:requirement)
    private String updateReqmtMvpUrl;

    //获取指定过程方法ID对应的需求编号
    private String retrieveReqmtSerialNoByLabel;

    //获取mvpId为指定的值的过程方法的Id
    private String retrieveMvpLabelUrl;

    //获取指定Id的项目的模板Id
    private String retrieveTemplateUrl;

    //获取指定hostSerialNo(项目编号)和templateId的需求编号
    private String retrieveReqmtSerialNoUrl;

    //获取MVP的Story
    private String retrieveMvpStoryContentUrl;

    //获取指定ID的模板名称
    private String retrieveTemplateNameUrl;

    //拖动(修改)mvp的Story
    private String updateMvpDashboardByStoryUrl;

    //获取项目的层级标记
    private String retrieveProjectLayerTypeUrl;

    //拖动(修改)mvp的reqmt
    private String updateMvpDashboardByReqmtUrl;

    //获取MVP requirement
    private String retrieveReqmtDashboardUrl;

    //获取MVP Story
    private String retrieveStoryDashboardUrl;
}
