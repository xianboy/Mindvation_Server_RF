package com.mdvns.mdvn.mdvnreward.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Component
@Data
@NoArgsConstructor
public class RtrvRewardDetailRequest {

    /*当前用户Id*/
    @NotNull(message = "用户Id不能为空")
    @Min(value = 1, message = "用户Id不能小于1")
    private Long staffId;
    /*查询参数*/
    @NotNull(message = "查询参数不能为空")
    private String criterion;
}
