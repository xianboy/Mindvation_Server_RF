package com.mdvns.mdvn.issue.domain;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RtrvStoryDetailRequest {
    @NotBlank(message = "请求参数错误，storyId不能为空")
    private String storyId;

    private String staffId;

    private List<String> remarks;

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public List<String> getRemarks() {
        return remarks;
    }

    public void setRemarks(List<String> remarks) {
        this.remarks = remarks;
    }
}
