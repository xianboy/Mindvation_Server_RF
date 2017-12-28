package com.mdvns.mdvn.websocket.domain;


import com.mdvns.mdvn.common.beans.Staff;
import org.springframework.stereotype.Component;


@Component
public class ServerPushInfo {

    private Integer uuId;
    private String subjectType;
    private String subjectId;
    private String type;
    private Integer oldProgress;
    private Integer newProgress;
    private String taskByStoryId;
    private Long createTime;
    private String initiatorId;//发起人
    private String recipientId;//接收人
    private Staff initiator;

    public String getTaskByStoryId() {
        return taskByStoryId;
    }

    public void setTaskByStoryId(String taskByStoryId) {
        this.taskByStoryId = taskByStoryId;
    }

    public Integer getOldProgress() {
        return oldProgress;
    }

    public void setOldProgress(Integer oldProgress) {
        this.oldProgress = oldProgress;
    }

    public Integer getNewProgress() {
        return newProgress;
    }

    public void setNewProgress(Integer newProgress) {
        this.newProgress = newProgress;
    }

    public Staff getInitiator() {
        return initiator;
    }

    public void setInitiator(Staff initiator) {
        this.initiator = initiator;
    }

    public Integer getUuId() {
        return uuId;
    }

    public void setUuId(Integer uuId) {
        this.uuId = uuId;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getInitiatorId() {
        return initiatorId;
    }

    public void setInitiatorId(String initiatorId) {
        this.initiatorId = initiatorId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    @Override
    public String toString() {
        return "ServerPush{" +
                "uuId=" + uuId +
                ", subjectType='" + subjectType + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", type='" + type + '\'' +
                ", createTime=" + createTime +
                ", initiatorId='" + initiatorId + '\'' +
                ", recipientId='" + recipientId + '\'' +
                '}';
    }
}
