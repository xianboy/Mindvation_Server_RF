package com.mdvns.mdvn.department.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class UpdateDepartmentRequest {

    /*部门id: 不是部门编号*/
    @NotNull(message = "id不能为空")
    @Min(value = 1, message = "id不能小于1")
    private Long id;
    @NotNull(message = "staffId不能为空")
    @Min(value = 1, message = "staffId不能小于1")
    private Long staffId;
    /*职位*/
    private List<String> positions;

}
