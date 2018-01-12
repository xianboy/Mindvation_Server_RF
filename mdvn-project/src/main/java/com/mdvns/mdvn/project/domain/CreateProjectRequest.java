package com.mdvns.mdvn.project.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateProjectRequest {
    /*创建人id*/
    @NotNull(message = "创建人Id不能为空")
    @Min(value = 1, message = "id不能小于1")
    private Long creatorId;
    /*项目名称*/
    @NotBlank(message = "项目名不能为空")
    private String name;
    /*项目描述*/
    @NotBlank(message = "项目描述不能为空")
    private String description;
    /*标签*/
    @Size(min = 1, message = "如果有标签, tags 必须有原始")
    private List<Long> tags;
    /*项目优先级*/
    private Integer priority;
    /*负责人*/
    private List<Long> leaders;
    /*项目开始日期*/
    private Long startDate;
    /*项目结束日期*/
    private Long endDate;
    /*项目所选的模板*/
    private List<Long> templates;
    /*项目可调整系数:是否可用Integer*/
    private Double contingency;
    /*项目附件: 多个附件的id*/
    private List<Long> attaches;
    /*项目层级结构类型*/
    private Integer layerType;
}
