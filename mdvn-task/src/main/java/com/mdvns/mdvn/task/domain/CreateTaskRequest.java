package com.mdvns.mdvn.task.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class CreateTaskRequest implements Serializable {

    /*创建人ID*/
    @NotNull(message = "创建人id不能为空.")
    private Long creatorId;

    /*被分配人id*/
    private Long assigneeId;

    /*关联主体编号*/
    @NotBlank(message = "hostSerialNo不能为空.")
    private String hostSerialNo;

    /*描述*/
    @NotBlank(message = "description不能为空")
    private String description;

    /*开始日期*/
    /*结束日期*/
    @NotNull(message = "startDate or endDate不能为空")
    private Long startDate, endDate;

    @NotNull(message = "交付件不能为空.")
    private Object delivery;

    /*项目编号*/
    private String projSerialNo;

    /* 层级结构类型 */
    @NotNull(message = "层级不能为空.")
    private Integer layerType;

}
