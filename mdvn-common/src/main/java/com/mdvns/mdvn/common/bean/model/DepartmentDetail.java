package com.mdvns.mdvn.common.bean.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 部门详情
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDetail implements Serializable {
    /*id*/
    private Long id;
    /*部门编号*/
    private String serialNo;
    /*部门名称*/
    private String name;
    /*创建时间*/
    private Long createTime;
    /*部门所包含的职位信息*/
    private List<PositionModel> positions;


}
