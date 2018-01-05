package com.mdvns.mdvn.template.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateLabelRequest {

    /*名称*/
    @NotBlank(message = "名称不能为空")
    private String name;

    /*子模块名称*/
    private List<String> subLabels;

}
