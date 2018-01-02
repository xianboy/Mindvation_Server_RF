package com.mdvns.mdvn.story.domain;

import com.mdvns.mdvn.common.bean.MemberRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateStoryRequest implements Serializable {

    /*创建人Id*/
    @NotNull(message = "创建人id不能为空")
    private Long creatorId;

    /*关联主体编号*/
    @NotBlank(message = "hostSerialNo 不能为空")
    private String hostSerialNo;

    /*项目编号*/
    @NotBlank(message = "hostSerialNo 不能为空")
    private String projHostSerialNo;

    /*模板id*/
    @NotNull(message = "模板id不能为空")
    private Long templateId;

    /*概要*/
    @NotBlank(message = "summary不能为空")
    private String summary;

    /*描述*/
    @NotBlank(message = "description不能为空")
    private String description;

    /*标签*/
    @Size(min = 1, message = "如果有标签, tags 必须有原始")
    private List<Long> tags;

    /*优先级*/
    @NotNull(message = "priority不能为空")
    private Integer priority;

    /*过程方法模块: 新件模块就是name, 否则就是id*/
    @NotNull(message = "过程方法不能为空")
    private Object functionLabel;

    /*成员*/
    @Size(min = 1, message = "如果有标签, members 必须有原始")
    private List<MemberRequest> members;

    /*开始日期*/
    /*结束日期*/
    @NotNull(message = "startDate or endDate不能为空")
    private Long startDate, endDate;

    /*用户故事点*/
    @NotNull(message = "story point不能为空")
    private Double storyPoint;

    /*附件*/
    private Long attachId;


}
