package com.mdvns.mdvn.task.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class UpdateProgressRequest implements Serializable{

    /*当前用户id*/
    @NotNull(message = "staffId不能为空")
    private Long staffId;

    /*taskId*/
    @NotNull(message = "更新对象id不能为空")
    private Long hostId;

    /*进度*/
    @NotNull(message = "进度不能为空")
    private Integer progress;

    /*备注*/
    private String comment;

}
