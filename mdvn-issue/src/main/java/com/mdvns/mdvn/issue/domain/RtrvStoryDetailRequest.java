package com.mdvns.mdvn.issue.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
@Data
public class RtrvStoryDetailRequest {
    @NotBlank(message = "请求参数错误，storyId不能为空")
    private String storyId;

    private Long staffId;

    private List<String> remarks;
}
