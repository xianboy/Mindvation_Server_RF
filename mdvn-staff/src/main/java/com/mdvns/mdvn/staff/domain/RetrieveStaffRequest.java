package com.mdvns.mdvn.staff.domain;

import com.mdvns.mdvn.common.bean.model.PageableCriteria;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class RetrieveStaffRequest implements Serializable {

    /*当前用户Id*/
    private Long staffId;

    //name包含字符串
    private String name;

    /*标签id*/
    private List<Long> tags;

    /*分页参数*/
    private PageableCriteria pageableCriteria;

    /*是否已删除*/
    private Integer isDeleted;
}
