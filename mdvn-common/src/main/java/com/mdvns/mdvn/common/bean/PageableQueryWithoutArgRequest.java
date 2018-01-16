package com.mdvns.mdvn.common.bean;

import com.mdvns.mdvn.common.bean.model.PageableCriteria;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class PageableQueryWithoutArgRequest implements Serializable {

    /*查询者Id*/
    @NotNull(message = "staffId不能为空")
    @Min(value = 1, message = "staffId不能小于1")
    private Long staffId;

    /*分页参数对象*/
    private PageableCriteria pageableCriteria;

}
