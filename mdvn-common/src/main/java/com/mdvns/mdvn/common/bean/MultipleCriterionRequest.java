package com.mdvns.mdvn.common.bean;

import com.mdvns.mdvn.common.bean.model.PageableCriteria;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 查询某个对象的详情(多个参数)
 */
@Data
@NoArgsConstructor
public class MultipleCriterionRequest implements Serializable {

    /*当前用户Id*/
    @NotNull(message = "用户Id不能为空")
    @Min(value = 1, message = "用户Id不能小于1")
    private Long staffId;
    /*查询参数*/
    @NotNull(message = "查询参数不能为空")
    private String criterion;

    private String otherCriterion;

    /*是否已删除*/
    private Integer isDeleted;

    /*分页对象*/
    private PageableCriteria pageableCriteria;

    public MultipleCriterionRequest(Long staffId, String criterion) {
        this.staffId = staffId;
        this.criterion = criterion;
    }

    public MultipleCriterionRequest(Long staffId, String criterion, Integer isDeleted) {
        this.staffId = staffId;
        this.criterion = criterion;
        this.isDeleted = isDeleted;
    }
}
