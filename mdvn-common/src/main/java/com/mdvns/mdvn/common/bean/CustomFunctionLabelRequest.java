package com.mdvns.mdvn.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomFunctionLabelRequest implements Serializable{

    /*当前用户Id*/
    @NotNull(message = "用户Id不能为空")
    @Min(value = 1, message = "用户Id不能小于1")
    private Long creatorId;

    @NotBlank(message = "上层关联主体编号不能为空")
    private String hostSerialNo;

    /*名称*/
    @NotBlank(message = "名称不能为空")
    private String name;

}
