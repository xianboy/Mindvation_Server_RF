package com.mdvns.mdvn.issue.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "url")
@Component
public class WebConfig {

    private String rtrvStaffInfoUrl;
    private String findTagInfoByIdUrl;
    private String rtrvCommentInfosUrl;
    private String rtrvCreatorIdUrl;
    private String sendMessageUrl;
    private String rtrvSRoleMembersUrl;
    private String rtrvReqmntMembersUrl;

    public String getRtrvSRoleMembersUrl() {
        return rtrvSRoleMembersUrl;
    }

    public void setRtrvSRoleMembersUrl(String rtrvSRoleMembersUrl) {
        this.rtrvSRoleMembersUrl = rtrvSRoleMembersUrl;
    }

    public String getRtrvReqmntMembersUrl() {
        return rtrvReqmntMembersUrl;
    }

    public void setRtrvReqmntMembersUrl(String rtrvReqmntMembersUrl) {
        this.rtrvReqmntMembersUrl = rtrvReqmntMembersUrl;
    }

    public String getSendMessageUrl() {
        return sendMessageUrl;
    }

    public void setSendMessageUrl(String sendMessageUrl) {
        this.sendMessageUrl = sendMessageUrl;
    }

    public String getRtrvCreatorIdUrl() {
        return rtrvCreatorIdUrl;
    }

    public void setRtrvCreatorIdUrl(String rtrvCreatorIdUrl) {
        this.rtrvCreatorIdUrl = rtrvCreatorIdUrl;
    }

    public String getRtrvStaffInfoUrl() {
        return rtrvStaffInfoUrl;
    }

    public void setRtrvStaffInfoUrl(String rtrvStaffInfoUrl) {
        this.rtrvStaffInfoUrl = rtrvStaffInfoUrl;
    }

    public String getFindTagInfoByIdUrl() {
        return findTagInfoByIdUrl;
    }

    public void setFindTagInfoByIdUrl(String findTagInfoByIdUrl) {
        this.findTagInfoByIdUrl = findTagInfoByIdUrl;
    }

    public String getRtrvCommentInfosUrl() {
        return rtrvCommentInfosUrl;
    }

    public void setRtrvCommentInfosUrl(String rtrvCommentInfosUrl) {
        this.rtrvCommentInfosUrl = rtrvCommentInfosUrl;
    }
}
