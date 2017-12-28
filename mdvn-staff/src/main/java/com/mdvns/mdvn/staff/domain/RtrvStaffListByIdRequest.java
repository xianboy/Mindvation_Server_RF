package com.mdvns.mdvn.staff.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@AllArgsConstructor
public class RtrvStaffListByIdRequest {
    private List<String> staffIdList;

}
