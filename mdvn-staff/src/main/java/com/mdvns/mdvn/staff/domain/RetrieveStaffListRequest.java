package com.mdvns.mdvn.staff.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveStaffListRequest {

    /*第几页*/
    private Integer page;

    /*本次查询获取几条数据*/
    private Integer pageSize;

    /*排序条件：字段名*/
    private String sortBy;

}

