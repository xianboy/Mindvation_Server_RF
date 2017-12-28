package com.mdvns.mdvn.websocket.domain.entity;


import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Component
public class ServerPush {

    @Id
    @GeneratedValue
    private Integer uuId;
    private String subjectType;//project、requirement、story、task、comment
    private String subjectId;
    private String type;//create、update、@
    private Integer oldProgress;//以前的进度
    private Integer newProgress;//现在的进度
    private String taskByStoryId;//task所属的storyId
    private Timestamp createTime;
    private String initiatorId;//发起人
    private String recipientId;//接收人

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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
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

    public String getTaskByStoryId() {
        return taskByStoryId;
    }

    public void setTaskByStoryId(String taskByStoryId) {
        this.taskByStoryId = taskByStoryId;
    }

    @Override
    public String toString() {
        return "ServerPush{" +
                "uuId=" + uuId +
                ", subjectType='" + subjectType + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", type='" + type + '\'' +
                ", oldProgress=" + oldProgress +
                ", newProgress=" + newProgress +
                ", taskByStoryId='" + taskByStoryId + '\'' +
                ", createTime=" + createTime +
                ", initiatorId='" + initiatorId + '\'' +
                ", recipientId='" + recipientId + '\'' +
                '}';
    }
}
