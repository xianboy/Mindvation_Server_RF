package com.mdvns.mdvn.dashboard.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateMvpRequest implements Serializable {

    /*创建人ID*/
    @NotNull(message = "creatorId不能为空")
    private Long creatorId;

    /*模板ID*/
    @NotNull(message = "templateId 不能为空")
    private Long templateId;

    /*MVP序列号*/
    @NotNull(message = "index不能为空")
    private Integer mvpIndex;

    /*内容Id*/
    @NotNull(message = "contents不能为空")
    private List<Long> contents;

}
