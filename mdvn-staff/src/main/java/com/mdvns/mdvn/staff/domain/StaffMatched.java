package com.mdvns.mdvn.staff.domain;

import com.mdvns.mdvn.common.bean.model.Tag;
import com.mdvns.mdvn.common.bean.model.TerseInfo;
import com.mdvns.mdvn.staff.domain.entity.Staff;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 根据标签查人
 */
@Data
@NoArgsConstructor
public class StaffMatched {

    /*员工信息*/
    private Staff staff;
    /*推荐度*/
    private Double recommendation;
    /*员工拥有的标签*/
    private List<TerseInfo> tags;



}
