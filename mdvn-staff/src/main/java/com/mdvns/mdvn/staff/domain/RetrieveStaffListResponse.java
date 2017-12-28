package com.mdvns.mdvn.staff.domain;

import com.mdvns.mdvn.staff.domain.entity.Staff;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveStaffListResponse {

    /*staff列表*/
    private List<Staff> staffs;
    //总数
    private Integer totalNumber;

}
