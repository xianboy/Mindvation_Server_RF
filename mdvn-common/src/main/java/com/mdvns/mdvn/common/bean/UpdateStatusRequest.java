package com.mdvns.mdvn.common.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class UpdateStatusRequest implements Serializable {

    /*当前用户Id*/
    @NotNull(message = "用户Id不能为空")
    @Min(value = 1, message = "用户Id不能小于1")
    private Long staffId;

    /*更新对象id*/
    @NotNull(message = "更新对象Id不能为空")
    @Min(value = 1, message = "更新对象Id不能小于1")
    private Long hostId;

    /*状态值*/
    @NotBlank(message = "更新的状态值不能为空")
    private String status;

}
