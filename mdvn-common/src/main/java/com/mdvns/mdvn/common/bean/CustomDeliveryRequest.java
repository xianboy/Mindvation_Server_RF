package com.mdvns.mdvn.common.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class CustomDeliveryRequest implements Serializable {

    /*创建人id*/
    @NotNull(message = "创建人id不能为空")
    private Long creatorId;

    /*关联主体编号*/
    @NotBlank(message = "hostSerialNo不能为空.")
    private String hostSerialNo;

    /*名称*/
    @NotBlank(message = "name不能为空.")
    private String name;

    /*类型*/
    @NotNull(message = "typeId不能为空.")
    private Integer typeId;

}
