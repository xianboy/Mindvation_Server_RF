package com.mdvns.mdvn.requirement.domain;

import com.mdvns.mdvn.common.bean.MemberRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateRequirementRequest {

    /*当前用户id*/
    @NotNull(message = "创建人Id不能为空")
    @Min(value = 1, message = "id不能小于1")
    private Long creatorId;

    /*项目id*/
    @NotBlank(message = "hostSerialNo不能为空")
    private String hostSerialNo;

    /*概要*/
    @NotBlank(message = "summary不能为空")
    private String summary;

    /*描述*/
    @NotBlank(message = "描述不能为空")
    private String description;

    /*标签*/
    @Size(min = 1, message = "如果有标签, tags 必须有原始")
    private List<Long> tags;

    /*优先级*/
    private Integer priority;

    /*模板id*/
    @NotNull(message = "templateId不能为空")
    @Min(value = 1, message = "templateId不能小于1")
    private Long templateId;

    /*过程方法模块:使用模板的模块id, 新件模块就是name*/
    @NotNull(message = "过程方法不能为空")
    private Object functionLabel;

    /*成员*/
    @Size(min = 1, message = "如果有标签, members 必须有原始")
    private List<MemberRequest> members;

    /*开始日期*/
    private Long startDate;

    /*结束日期*/
    private Long endDate;

    /*需求附件: 多个附件的id*/
    private List<Long> attaches;

    /*项目层级结构类型*/
    private Integer layerType;

}
