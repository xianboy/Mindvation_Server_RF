package com.mdvns.mdvn.common.bean.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class FunctionLabelModel implements Serializable {

    private Long id;

    /*创建人ID*/
    @NotNull(message = "id不能为空")
    @Min(value = 1, message = "id不能小于1")
    private Long creatorId;

    /*label所属对象的编号(如果是FunctionLabel,则是id)*/
    @NotBlank(message = "hostSerialNo不能为空")
    private String hostSerialNo;

    /*名称*/
    @NotBlank(message = "名称不能为空")
    private String name;

    /*创建时间*/
    private Long createTime;

    /*是否已删除*/
    @JsonIgnore
    private Integer isDeleted;

    /*子模块名称*/
    private List<String> subLabels;

}
