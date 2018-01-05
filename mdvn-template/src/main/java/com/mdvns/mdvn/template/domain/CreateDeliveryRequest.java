package com.mdvns.mdvn.template.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class CreateDeliveryRequest implements Serializable {

    /*类型:1,2,3*/
    @NotNull(message = "typeId不能为空")
    private Integer typeId;

    /*交付件名称*/
    @NotBlank(message = "name不能为空")
    private String name;


}
