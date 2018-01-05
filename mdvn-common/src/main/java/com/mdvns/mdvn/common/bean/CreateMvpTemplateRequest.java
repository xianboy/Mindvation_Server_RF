package com.mdvns.mdvn.common.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateMvpTemplateRequest implements Serializable {

    /*创建人id*/
    @NotNull(message = "创建人的id不能为空")
    private Long creatorId;

    /*上层模块(模板/项目)编号*/
    @NotBlank(message = "hostSerialNo 不能为空")
    private String hostSerialNo;

    /*MVP序列号*/
    @NotNull(message = "index不能为空")
    private Integer index;

    /*内容Id*/
    @NotNull(message = "contents不能为空")
    private List<Long> contents;





}
