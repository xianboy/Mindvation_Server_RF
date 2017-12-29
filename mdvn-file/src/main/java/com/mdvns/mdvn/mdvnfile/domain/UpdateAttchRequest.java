package com.mdvns.mdvn.mdvnfile.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdateAttchRequest {

    /*AttchInfo表中附件的Id*/
    private Integer attchId;

    /*附件主体的Id*/
    private String subjectid;

    private List<String> remarks;

    public Integer getAttchId() {
        return attchId;
    }

    public void setAttchId(Integer attchId) {
        this.attchId = attchId;
    }

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

    public UpdateAttchRequest() {
        super();
    }

    public List<String> getRemarks() {
        return remarks;
    }

    public void setRemarks(List<String> remarks) {
        this.remarks = remarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateAttchRequest)) return false;

        UpdateAttchRequest that = (UpdateAttchRequest) o;

        if (getAttchId() != null ? !getAttchId().equals(that.getAttchId()) : that.getAttchId() != null) return false;
        return getSubjectid() != null ? getSubjectid().equals(that.getSubjectid()) : that.getSubjectid() == null;
    }

    @Override
    public int hashCode() {
        int result = getAttchId() != null ? getAttchId().hashCode() : 0;
        result = 31 * result + (getSubjectid() != null ? getSubjectid().hashCode() : 0);
        return result;
    }
}
