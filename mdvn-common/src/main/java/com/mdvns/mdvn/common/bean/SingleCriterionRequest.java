package com.mdvns.mdvn.common.bean;

import com.mdvns.mdvn.common.bean.model.PageableCriteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 查询某个对象的详情
 */
@Data
@NoArgsConstructor
public class SingleCriterionRequest implements Serializable {

    /*当前用户Id*/
    @NotNull(message = "用户Id不能为空")
    @Min(value = 1, message = "用户Id不能小于1")
    private Long staffId;
    /*查询参数*/
    @NotNull(message = "查询参数不能为空")
    private String criterion;

    /*是否已删除*/
    private Integer isDeleted;

    /*分页对象*/
    private PageableCriteria pageableCriteria;

    public SingleCriterionRequest(Long staffId, String criterion) {
        this.staffId = staffId;
        this.criterion = criterion;
    }

    public SingleCriterionRequest(Long staffId, String criterion, Integer isDeleted) {
        this.staffId = staffId;
        this.criterion = criterion;
        this.isDeleted = isDeleted;
    }
}
