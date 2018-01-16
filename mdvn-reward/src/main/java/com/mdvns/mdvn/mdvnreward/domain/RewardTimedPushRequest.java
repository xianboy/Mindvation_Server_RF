package com.mdvns.mdvn.mdvnreward.domain;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Component
@Data
@NoArgsConstructor
public class RewardTimedPushRequest {
    /*查询者Id*/
    @NotNull(message = "staffId不能为空")
    @Min(value = 1, message = "staffId不能小于1")
    private Long staffId;
    private String serialNo;//悬赏榜编号
    private Long pushTime;
}
