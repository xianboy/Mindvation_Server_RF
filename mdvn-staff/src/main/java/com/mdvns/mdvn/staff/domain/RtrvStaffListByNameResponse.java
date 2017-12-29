package com.mdvns.mdvn.staff.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RtrvStaffListByNameResponse {

    private List<StaffMatched> staffMatched;

    private Long totalNumber;

    private List<String> remarks;

    public List<StaffMatched> getStaffMatched() {
        return staffMatched;
    }

    public void setStaffMatched(List<StaffMatched> staffMatched) {
        this.staffMatched = staffMatched;
    }

    public Long getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Long totalNumber) {
        this.totalNumber = totalNumber;
    }

    public List<String> getRemarks() {
        return remarks;
    }

    public void setRemarks(List<String> remarks) {
        this.remarks = remarks;
    }
}
