package com.mdvns.mdvn.mdvnreward.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@Data
@NoArgsConstructor
public class CreateRewardRequest {

    /*创建人Id*/
    @NotNull(message = "创建人id不能为空")
    private Long creatorId;
    private String name;
    private String content;
    private Integer welfareScore;//福利积分
    private Integer contribution;//贡献度
    private Long tagId;
}
