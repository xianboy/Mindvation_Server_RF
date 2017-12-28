package com.mdvns.mdvn.department.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateDeptRequest {

    /*部门名称*/
    @NotBlank(message = "部门名称不能为空")
    private String name;
    /*创建者id*/
    @NotNull(message = "创建人不能为空")
    private Long creatorId;
    /*职位信息*/
    private List<String> positions;

}
