package com.mdvns.mdvn.mdvnreward.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@Data
@NoArgsConstructor
public class ReceiveARewardRequest {

    /*创建人id*/
    @NotNull(message = "创建人的id不能为空")
    private Long staffId;
    /*悬赏榜编号*/
    @NotBlank(message = "serialNo 不能为空")
    private String serialNo;

}
