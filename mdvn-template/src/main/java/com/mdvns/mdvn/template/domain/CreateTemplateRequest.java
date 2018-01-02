package com.mdvns.mdvn.template.domain;

import com.mdvns.mdvn.common.constant.MdvnConstant;
import com.mdvns.mdvn.template.domain.entity.Delivery;
import com.mdvns.mdvn.template.domain.entity.FunctionLabel;
import com.mdvns.mdvn.template.domain.entity.IterationTemplate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateTemplateRequest {
    /*创建人id*/
    @NotNull(message = "创建人的id不能为空")
    @Min(value = 1, message = "id的值不能小于1")
    private Long creatorId;

    /*名称*/
    @NotBlank(message = "名称不能为空")
    private String name;

    /*行业id*/
    @NotNull(message = "industryId不能为空 ")
    @Min(value = 1, message = "industryId 不能小于1")
    private Long industryId;

    /*后加的字段，1~7随机给一个数字*/
    @Min(value = 1, message = "style的值不能小于1")
    private Integer style = MdvnConstant.ONE;


    //过程方法模块
    @NotEmpty(message = "如果要添加FunctionLabel, labels必须有元素")
    @NotNull(message = "labels不能为空")
    private List<CreateLabelRequest> labels;

    /*模板角色*/
    @NotEmpty(message = "如果要添加role, roleNames必须有元素")
    private List<String> roleNames;

    /*迭代模板*/
    @NotEmpty(message = "如果要添加IterationTemplate, itTemplates必须有元素")
    @NotNull(message = "itTemplates不能为空")
    private List<IterationTemplate> itTemplates;

    /*交付件*/
    @NotEmpty(message = "如果要添加Deliverable, deliverables必须有元素")
    private List<Delivery> deliverables;



}
